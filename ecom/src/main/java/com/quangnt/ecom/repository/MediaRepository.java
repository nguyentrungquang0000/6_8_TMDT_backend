package com.quangnt.ecom.repository;

import com.quangnt.ecom.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public interface MediaRepository extends JpaRepository<Media, String> {
    List<Media> findByIdIn(List<String> mediaIds);

    Set<Media> findByStatusFalseAndCreatedAtBefore(Instant offsetDateTime);
}
