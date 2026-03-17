package edu.ntt.hotelbookingplatform.service;

import edu.ntt.hotelbookingplatform.dto.in.BookingRequestDTO;
import edu.ntt.hotelbookingplatform.dto.out.BookingResponseDTO;

import java.util.List;
import java.time.LocalDate;

public interface BookingService {

    BookingResponseDTO createBooking(Long userId, BookingRequestDTO request);

    BookingResponseDTO cancelBooking(Long bookingId, Long requestingUserId);

    List<BookingResponseDTO> getBookingsByUser(Long userId);

    BookingResponseDTO getBookingById(Long bookingId);

    List<BookingResponseDTO> getUpcomingBookings();

    boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut);
}
