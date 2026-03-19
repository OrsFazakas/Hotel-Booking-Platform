package edu.ntt.hotelbookingplatform.repository;

import edu.ntt.hotelbookingplatform.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    List<Booking> findByUserIdAndStatus(Long userId, Booking.BookingStatus status);

    @Query("""
        SELECT b FROM Booking b
        WHERE b.checkInDate >= :today
        AND b.status <> 'CANCELLED'
        ORDER BY b.checkInDate ASC
    """)
    List<Booking> findUpcomingBookings(@Param("today") LocalDate today);

    @Query("SELECT b FROM Booking b WHERE b.checkInDate = :date AND b.status = 'CONFIRMED'")
    List<Booking> findArrivalsByDate(@Param("date") LocalDate date);

    @Query("""
        SELECT COUNT(b) > 0 FROM Booking b
        WHERE b.room.id = :roomId
        AND b.status <> 'CANCELLED'
        AND b.checkInDate < :checkOut
        AND b.checkOutDate > :checkIn
    """)
    boolean existsOverlappingBooking(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );

    @Query("" +
            "SELECT SUM(b.totalPrice) FROM Booking b " +
            "WHERE b.status = 'CONFIRMED' " +
            "AND b.checkInDate " +
            "BETWEEN :start AND :end")
    BigDecimal calculateRevenue(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT COUNT(DISTINCT b.room.id) " +
            "FROM Booking b " +
            "WHERE b.status = 'CONFIRMED' " +
            "AND b.checkInDate < :end " +
            "AND b.checkOutDate > :start")
    long countOccupiedRooms(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("""
        SELECT COUNT(b) > 0 FROM Booking b
        WHERE b.room.id = :roomId
        AND b.id <> :excludeId
        AND b.status <> 'CANCELLED'
        AND b.checkInDate < :checkOut
        AND b.checkOutDate > :checkIn
    """)
    boolean existsOverlappingBookingExcluding(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            @Param("excludeId") Long excludeId
    );
}

