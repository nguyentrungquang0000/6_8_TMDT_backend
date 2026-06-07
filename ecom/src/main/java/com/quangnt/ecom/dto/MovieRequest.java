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
public class MovieRequest {
    private String title;
    private String genre;
    private Integer duration;
    private String director;
    private String movieCast;
    private String description;
    private String posterId;
    private LocalDate releaseDate;
    private String teaserId;
    private MovieStatus status;
    private Boolean isTrending;
}