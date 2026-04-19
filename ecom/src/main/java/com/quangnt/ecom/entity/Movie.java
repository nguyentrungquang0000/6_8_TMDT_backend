package com.quangnt.ecom.entity;

import com.quangnt.common.base.BaseEntity;
import com.quangnt.ecom.dto.MovieStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private String director;

    @Column(columnDefinition = "TEXT")
    private String movieCast;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poster_media_id")
    private Media poster;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovieStatus status;

    @Column(name = "teaser_url", length = 500)
    private String teaserUrl;

    @Column(name = "review_url", length = 500)
    private String reviewUrl;
}
