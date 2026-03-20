package edu.ntt.hotelbookingplatform.service;

import edu.ntt.hotelbookingplatform.dto.in.BookingRequestDTO;
import edu.ntt.hotelbookingplatform.dto.out.BookingResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

public interface BookingService {

    BookingResponseDTO createBooking(Long userId, BookingRequestDTO request);

    BookingResponseDTO cancelBooking(Long bookingId, Long requestingUserId);

    List<BookingResponseDTO> getBookingsByUser(Long userId);

    BookingResponseDTO getBookingById(Long bookingId);

    List<BookingResponseDTO> getUpcomingBookings();

    List<BookingResponseDTO> getBookingsByUserAndStatus(Long userId, String status);
    List<BookingResponseDTO> getArrivals(LocalDate date);

    BigDecimal getRevenue(LocalDate start, LocalDate end);

    Map<String, Object> getOccupancyReport(LocalDate start, LocalDate end);

    boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut);
}
