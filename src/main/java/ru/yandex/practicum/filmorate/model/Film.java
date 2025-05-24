package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


@Data
@NoArgsConstructor
public class Film {

    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Mpa mpa;

    private Collection<Genre> genres = List.of();
    private Collection<Director> directors = List.of();

    public Film(String name, String description, Integer duration, LocalDate releaseDate, Mpa mpa) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.mpa = mpa;
    }
}
