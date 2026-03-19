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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock private BookingRepository bookingRepository;
    @Mock private RoomRepository roomRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Users testUser;
    private Users adminUser;
    private Room testRoom;
    private Booking testBooking;

    @BeforeEach
    void setUp() {
        testUser = new Users();
        testUser.setId(1L);
        testUser.setRole("CUSTOMER");

        adminUser = new Users();
        adminUser.setId(2L);
        adminUser.setRole("ADMIN");

        testRoom = Room.builder()
                .id(1L)
                .pricePerNight(new BigDecimal("100.0"))
                .roomNumber("101")
                .build();

        testBooking = Booking.builder()
                .id(10L)
                .user(testUser)
                .room(testRoom)
                .status(Booking.BookingStatus.CONFIRMED)
                .build();
    }

    @Test
    void createBooking_ShouldCalculateCorrectPrice() {
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3); // 2 éjszaka
        BookingRequestDTO request = new BookingRequestDTO(1L, checkIn, checkOut);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(testRoom));
        when(bookingRepository.existsOverlappingBooking(any(), any(), any())).thenReturn(false);
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        BookingResponseDTO response = bookingService.createBooking(1L, request);

        assertEquals(new BigDecimal("200.0"), response.totalPrice());
    }

    @Test
    void createBooking_ShouldThrowException_WhenDatesOverlap() {
        BookingRequestDTO request = new BookingRequestDTO(1L, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(testRoom));
        when(bookingRepository.existsOverlappingBooking(any(), any(), any())).thenReturn(true);

        assertThrows(RoomNotAvailableException.class, () -> bookingService.createBooking(1L, request));
    }

    @Test
    void cancelBooking_ShouldAllow_WhenUserIsOwner() {
        when(bookingRepository.findById(10L)).thenReturn(Optional.of(testBooking));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(bookingRepository.save(any())).thenReturn(testBooking);

        BookingResponseDTO response = bookingService.cancelBooking(10L, 1L);

        assertEquals("CANCELLED", response.status());
    }

    @Test
    void cancelBooking_ShouldThrowException_WhenUserIsNotOwnerOrAdmin() {
        Users stranger = new Users(); stranger.setId(3L); stranger.setRole("CUSTOMER");
        when(bookingRepository.findById(10L)).thenReturn(Optional.of(testBooking));
        when(userRepository.findById(3L)).thenReturn(Optional.of(stranger));

        assertThrows(AccessDeniedException.class, () -> bookingService.cancelBooking(10L, 3L));
    }

    @Test
    void cancelBooking_ShouldAllow_WhenUserIsAdmin() {
        when(bookingRepository.findById(10L)).thenReturn(Optional.of(testBooking));
        when(userRepository.findById(2L)).thenReturn(Optional.of(adminUser));
        when(bookingRepository.save(any())).thenReturn(testBooking);

        BookingResponseDTO response = bookingService.cancelBooking(10L, 2L);

        assertEquals("CANCELLED", response.status());
    }

    @Test
    void getBookingsByUserAndStatus_ShouldReturnFilteredList() {
        when(bookingRepository.findByUserIdAndStatus(1L, Booking.BookingStatus.CONFIRMED))
                .thenReturn(List.of(testBooking));

        List<BookingResponseDTO> result = bookingService.getBookingsByUserAndStatus(1L, "CONFIRMED");

        assertFalse(result.isEmpty());
        verify(bookingRepository).findByUserIdAndStatus(1L, Booking.BookingStatus.CONFIRMED);
    }

    @Test
    void getArrivals_ShouldCallRepository() {
        LocalDate date = LocalDate.now();
        when(bookingRepository.findArrivalsByDate(date)).thenReturn(List.of(testBooking));

        List<BookingResponseDTO> result = bookingService.getArrivals(date);

        assertEquals(1, result.size());
        verify(bookingRepository).findArrivalsByDate(date);
    }


    @Test
    void getRevenue_ShouldReturnZero_WhenNoRevenue() {
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusMonths(1);
        when(bookingRepository.calculateRevenue(start, end)).thenReturn(null);

        BigDecimal revenue = bookingService.getRevenue(start, end);

        assertEquals(BigDecimal.ZERO, revenue);
    }

    @Test
    void getOccupancyReport_ShouldCalculateCorrectRate() {
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(1);

        when(roomRepository.count()).thenReturn(10L);
        when(bookingRepository.countOccupiedRooms(start, end)).thenReturn(7L);

        Map<String, Object> report = bookingService.getOccupancyReport(start, end);

        assertEquals("70.00%", report.get("occupancyRate"));
        assertEquals(7L, report.get("occupiedRooms"));
        assertEquals(10L, report.get("totalRooms"));
    }
}