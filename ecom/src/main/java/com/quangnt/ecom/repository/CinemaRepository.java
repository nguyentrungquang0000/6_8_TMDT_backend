package com.quangnt.ecom.repository;

import com.quangnt.ecom.entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CinemaRepository extends JpaRepository<Cinema, Integer> {
    List<Cinema> findByIdIn(Set<Integer> ids);

    @Query("SELECT c FROM Cinema c WHERE c.isDeleted = false")
    List<Cinema> findAllByDeletedIsFalse();
}
