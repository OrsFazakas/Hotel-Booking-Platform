package edu.ntt.hotelbookingplatform.repository;

import edu.ntt.hotelbookingplatform.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    @Query("""
        SELECT b FROM Booking b
        WHERE b.checkInDate >= :today
        AND b.status <> 'CANCELLED'
        ORDER BY b.checkInDate ASC
    """)
    List<Booking> findUpcomingBookings(@Param("today") LocalDate today);

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

