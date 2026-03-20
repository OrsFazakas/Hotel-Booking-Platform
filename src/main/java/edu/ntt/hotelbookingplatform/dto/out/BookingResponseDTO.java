package edu.ntt.hotelbookingplatform.dto.out;

import edu.ntt.hotelbookingplatform.model.Booking;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookingResponseDTO(
        Long id,
        Long userId,
        String userEmail,
        Long roomId,
        String roomNumber,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        BigDecimal totalPrice,
        String status
) {
    public static BookingResponseDTO from(Booking booking) {
        return new BookingResponseDTO(
                booking.getId(),
                booking.getUser().getId(),
                booking.getUser().getEmail(),
                booking.getRoom().getId(),
                booking.getRoom().getRoomNumber(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getTotalPrice(),
                booking.getStatus().name()
        );
    }
}