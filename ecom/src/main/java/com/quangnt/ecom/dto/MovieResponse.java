package com.quangnt.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {
    private Integer id;
    private String title;
    private String genre;
    private Integer duration;
    private String director;
    private String cast;
    private String description;
    private String posterMediaId;
    private LocalDate releaseDate;
    private MovieStatus status;
    private String teaserUrl;
    private String reviewUrl;
    private Instant createdAt;
    private UUID createdBy;
    private Instant updatedAt;
    private UUID updatedBy;
}