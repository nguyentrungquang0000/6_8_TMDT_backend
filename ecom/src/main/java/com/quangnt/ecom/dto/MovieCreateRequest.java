package com.quangnt.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieCreateRequest {
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
}