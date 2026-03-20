package edu.ntt.hotelbookingplatform.controller;

import edu.ntt.hotelbookingplatform.dto.in.BookingRequestDTO;
import edu.ntt.hotelbookingplatform.dto.out.BookingResponseDTO;
import edu.ntt.hotelbookingplatform.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings", description = "Booking management endpoints")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @Operation(summary = "Create a new booking")
    public ResponseEntity<BookingResponseDTO> createBooking(
            @RequestParam Long userId,
            @Valid @RequestBody BookingRequestDTO request
    ) {
        BookingResponseDTO response = bookingService.createBooking(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @Operation(summary = "Cancel a booking")
    public ResponseEntity<BookingResponseDTO> cancelBooking(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(bookingService.cancelBooking(id, userId));
    }

    @GetMapping("/arrivals")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all confirmed arrivals for a specific date")
    public ResponseEntity<List<BookingResponseDTO>> getArrivals(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(bookingService.getArrivals(date));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @Operation(summary = "Get a booking by ID")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @Operation(summary = "Get bookings for a user (optional filter by status)")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByUser(
            @PathVariable Long userId,
            @RequestParam(required = false) String status) {

        if (status != null && !status.isEmpty()) {
            return ResponseEntity.ok(bookingService.getBookingsByUserAndStatus(userId, status));
        }
        return ResponseEntity.ok(bookingService.getBookingsByUser(userId));
    }

    @GetMapping("/upcoming")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all upcoming bookings (Admin only)")
    public ResponseEntity<List<BookingResponseDTO>> getUpcomingBookings() {
        return ResponseEntity.ok(bookingService.getUpcomingBookings());
    }

    @GetMapping("/statistics/revenue")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Calculate total revenue for a period")
    public ResponseEntity<BigDecimal> getRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(bookingService.getRevenue(start, end));
    }

    @GetMapping("/statistics/occupancy")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get occupancy rate for a period")
    public ResponseEntity<Map<String, Object>> getOccupancy(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(bookingService.getOccupancyReport(start, end));
    }
    @GetMapping("/availability")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @Operation(summary = "Check if a room is available for a date range")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam Long roomId,
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut
    ) {
        return ResponseEntity.ok(bookingService.isRoomAvailable(roomId, checkIn, checkOut));
    }

}
