package com.quangnt.ecom.entity;

import com.quangnt.common.base.BaseEntity;
import com.quangnt.ecom.dto.MovieStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
public class Movie extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, length = 255)
    private String genre;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false, length = 255)
    private String director;

    @Column(columnDefinition = "TEXT")
    private String movieCast;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String posterId;
    private String teaserId;
    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovieStatus status = MovieStatus.COMING_SOON;
}
