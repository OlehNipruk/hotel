package com.ua.robot.hotel.repository;

import com.ua.robot.hotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT r FROM Room r WHERE r.id NOT IN :bookedRoomIds")
    List<Room> findAvailableRooms(@Param("bookedRoomIds") List<Long> bookedRoomIds);


}