package edu.ntt.hotelbookingplatform.repository;

import edu.ntt.hotelbookingplatform.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {


}
