package com.quangnt.ecom.repository;

import com.quangnt.ecom.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    @Query("""
    SELECT r
    FROM Room r
    JOIN Cinema c ON r.cinemaId = c.id
    WHERE r.isDeleted = false
      AND c.isDeleted = false
      AND (
            (:cinemaId IS NOT NULL AND r.cinemaId = :cinemaId)
            OR (:cinemaId IS NULL AND :#{@tenantProvider.getTenantId()} IS NOT NULL
                AND r.cinemaId = :#{@tenantProvider.getTenantId()})
          )
    """)
    Page<Room> search(@Param("cinemaId") Integer cinemaId,
                      Pageable pageable);

    List<Room> findAllByIdIn(List<Integer> roomIds);
}
