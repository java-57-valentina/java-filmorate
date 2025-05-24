package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.stream.Collectors;

@UtilityClass
public class FilmMapper {

    public Film mapToFilm(FilmDto request, Mpa mpaDto) {
        Film film = new Film();
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setReleaseDate(request.getReleaseDate());
        film.setDuration(request.getDuration());
        film.setMpa(new Mpa(request.getMpa().getId(), mpaDto.getName()));

        if (request.getGenres() != null) {
            film.setGenres(
                    request.getGenres().stream()
                            .map(GenreMapper::mapToGenre)
                            .collect(Collectors.toSet()));
        }


        if (request.getDirectors() != null) {
            film.setDirectors(request.getDirectors().stream()
                    .map(DirectorMapper::mapToDirector).collect(Collectors.toSet()));
        }

        return film;
    }

    public FilmResponseDto mapToFilmDto(Film film) {
        MpaDto mpa = MpaMapper.mapToDto(film.getMpa());

        return new FilmResponseDto(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                mpa,
                film.getGenres().stream()
                        .map(GenreMapper::mapToGenreDto)
                        .collect(Collectors.toList()),
                film.getDirectors()
        );
    }
}
