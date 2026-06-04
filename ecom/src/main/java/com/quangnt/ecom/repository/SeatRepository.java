package com.quangnt.ecom.repository;

import com.quangnt.ecom.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Integer> {
    List<Seat> findAllByRoomId(Integer roomId);

    void deleteAllByRoomId(Integer id);
}
