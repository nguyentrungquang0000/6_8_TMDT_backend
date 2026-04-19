package com.quangnt.ecom.repository;

import com.quangnt.ecom.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface MediaRepository extends JpaRepository<Media, String> {
    List<Media> findByIdIn(List<UUID> mediaIds);

    Set<Media> findByStatusFalseAndCreatedAtBefore(Instant offsetDateTime);
}
