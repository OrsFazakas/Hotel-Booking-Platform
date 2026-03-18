package edu.ntt.hotelbookingplatform.service;

import edu.ntt.hotelbookingplatform.dto.in.BookingRequestDTO;
import edu.ntt.hotelbookingplatform.dto.out.BookingResponseDTO;
import edu.ntt.hotelbookingplatform.exception.ResourceNotFoundException;
import edu.ntt.hotelbookingplatform.exception.RoomNotAvailableException;
import edu.ntt.hotelbookingplatform.model.Booking;
import edu.ntt.hotelbookingplatform.model.Room;
import edu.ntt.hotelbookingplatform.model.Users;
import edu.ntt.hotelbookingplatform.repository.BookingRepository;
import edu.ntt.hotelbookingplatform.repository.RoomRepository;
import edu.ntt.hotelbookingplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingResponseDTO createBooking(Long userId, BookingRequestDTO request) {
        if (!request.checkInDate().isBefore(request.checkOutDate())) {
            throw new IllegalArgumentException("Check-in date must be before check-out date");
        }

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + request.roomId()));

        // 3. Check availability
        boolean conflict = bookingRepository.existsOverlappingBooking(
                room.getId(), request.checkInDate(), request.checkOutDate()
        );
        if (conflict) {
            throw new RoomNotAvailableException(
                    "Room " + room.getRoomNumber() + " is not available for the selected dates"
            );
        }

        long nights = ChronoUnit.DAYS.between(request.checkInDate(), request.checkOutDate());
        BigDecimal totalPrice = room.getPricePerNight().multiply(BigDecimal.valueOf(nights));

        Booking booking = Booking.builder()
                .user(user)
                .room(room)
                .checkInDate(request.checkInDate())
                .checkOutDate(request.checkOutDate())
                .totalPrice(totalPrice)
                .status(Booking.BookingStatus.CONFIRMED)
                .build();

        return BookingResponseDTO.from(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingResponseDTO cancelBooking(Long bookingId, Long requestingUserId) {
        Booking booking = findBookingOrThrow(bookingId);

        Users requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + requestingUserId));

        boolean isOwner = booking.getUser().getId().equals(requestingUserId);
        boolean isAdmin = requestingUser.getRole().equalsIgnoreCase("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new org.springframework.security.access.AccessDeniedException("You don't have permission to cancel this booking");
        }

        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking is already cancelled");
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        return BookingResponseDTO.from(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByUserAndStatus(Long userId, String status) {
        Booking.BookingStatus bStatus = Booking.BookingStatus.valueOf(status.toUpperCase());
        return bookingRepository.findByUserIdAndStatus(userId, bStatus)
                .stream().map(BookingResponseDTO::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found: " + userId);
        }
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(BookingResponseDTO::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getRevenue(LocalDate start, LocalDate end) {
        BigDecimal revenue = bookingRepository.calculateRevenue(start, end);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getOccupancyReport(LocalDate start, LocalDate end) {
        long totalRooms = roomRepository.count();
        long occupiedRooms = bookingRepository.countOccupiedRooms(start, end);
        double rate = totalRooms > 0 ? (double) occupiedRooms / totalRooms * 100 : 0;

        return Map.of(
                "occupancyRate", String.format("%.2f%%", rate),
                "occupiedRooms", occupiedRooms,
                "totalRooms", totalRooms
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getArrivals(LocalDate date) {
        return bookingRepository.findArrivalsByDate(date).stream().map(BookingResponseDTO::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDTO getBookingById(Long bookingId) {
        return BookingResponseDTO.from(findBookingOrThrow(bookingId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getUpcomingBookings() {
        return bookingRepository.findUpcomingBookings(LocalDate.now())
                .stream()
                .map(BookingResponseDTO::from)
                .toList();
    }

    private Booking findBookingOrThrow(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));
    }


}
