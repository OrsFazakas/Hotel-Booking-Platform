package edu.ntt.hotelbookingplatform.repository;

import edu.ntt.hotelbookingplatform.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {

    boolean existsByRoomNumber(String roomNumber);

    @Query("""
        SELECT r FROM Room r 
        WHERE r.id NOT IN (
            SELECT b.room.id FROM Booking b 
            WHERE b.status <> 'CANCELLED' 
            AND b.checkInDate < :checkOut 
            AND b.checkOutDate > :checkIn
        )
        AND (:capacity IS NULL OR r.capacity >= :capacity)
        AND (:type IS NULL OR r.type = :type)
    """)
    List<Room> findAvailableRoomsFiltered(
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            @Param("capacity") Integer capacity,
            @Param("type") String type
    );

}
