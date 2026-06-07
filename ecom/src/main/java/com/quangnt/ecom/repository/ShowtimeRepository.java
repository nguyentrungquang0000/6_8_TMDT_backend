package com.quangnt.ecom.repository;

import com.quangnt.ecom.dto.ShowtimeStatus;
import com.quangnt.ecom.entity.Showtime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ShowtimeRepository extends JpaRepository<Showtime, Integer> {
    boolean existsByRoomId(Integer id);

    @Query("""
        SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END
        FROM Showtime s
        WHERE s.roomId = :roomId
        AND (:startTime < s.endTime AND :endTime > s.startTime)
    """)
    boolean checkTime(@Param("startTime") LocalDateTime startTime,
                      @Param("endTime") LocalDateTime endTime,
                      @Param("roomId") Integer roomId);

    @Query("""
        SELECT s FROM Showtime s
        JOIN Movie m ON s.movieId = m.id
        JOIN Room r ON r.id = s.roomId
        WHERE (:movieName IS NULL OR :movieName = '' OR lower(m.title) LIKE lower(concat('%', :movieName, '%')))
           AND ((:cinemaId IS NULL AND (:#{@tenantProvider.getTenantId()} IS NULL OR r.cinemaId = :#{@tenantProvider.getTenantId()})) OR r.cinemaId = :cinemaId)
           AND (:status IS NULL OR s.status = :status)
           AND (
               CAST(:date AS date) IS NULL
               OR CAST(s.startTime AS date) = :date
           )
           AND s.isDeleted = false
    """)
    Page<Showtime> search(
            @Param("movieName") String movieName,
            @Param("cinemaId") Integer cinemaId,
            @Param("status") ShowtimeStatus status,
            @Param("date") LocalDate date,
            Pageable pageable
    );
}
