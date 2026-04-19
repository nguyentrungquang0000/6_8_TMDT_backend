package com.quangnt.ecom.repository;

import com.quangnt.ecom.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
}
