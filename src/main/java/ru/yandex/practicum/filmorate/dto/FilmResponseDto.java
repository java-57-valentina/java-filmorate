package ru.yandex.practicum.filmorate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collection;

@Data
@AllArgsConstructor
public class FilmResponseDto {
    Long id;
    String name;
    String description;
    Integer duration;
    LocalDate releaseDate;
    RatingDto mpa;
    Collection<GenreDto> genres;
}
