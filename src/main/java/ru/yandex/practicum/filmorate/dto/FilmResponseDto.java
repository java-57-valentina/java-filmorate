package ru.yandex.practicum.filmorate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collection;

@Data
@AllArgsConstructor
public class FilmResponseDto {
    private Long id;
    private String name;
    private String description;
    private Integer duration;
    private LocalDate releaseDate;
    private MpaDto mpa;
    private Collection<GenreDto> genres;
}
