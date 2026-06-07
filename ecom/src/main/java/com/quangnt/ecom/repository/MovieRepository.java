package com.quangnt.ecom.repository;

import com.quangnt.ecom.dto.MovieStatus;
import com.quangnt.ecom.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
    @Query("""
        SELECT m FROM Movie m
        WHERE m.isDeleted = false
            AND (:keyword IS NULL OR :keyword = '' OR LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND (:status IS NULL OR m.status = :status)
    """)
    Page<Movie> search(@Param("keyword") String keyword,
                       @Param("status")MovieStatus status,
                       Pageable pageable);

    List<Movie> findAllByIdIn(List<Integer> movieIds);

    @Query("SELECT m FROM Movie m WHERE m.isTrending = true AND m.isDeleted = false order by m.trendingOrder asc")
    List<Movie> findAllByIsTrendingAndDeletedFalse();
}
