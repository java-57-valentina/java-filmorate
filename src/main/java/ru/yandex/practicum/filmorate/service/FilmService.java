package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.FilmResponseDto;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;

    public Collection<FilmResponseDto> getTop(int count) {
        return filmStorage.getTop(count).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public Collection<FilmResponseDto> getAll() {
        return filmStorage.getAll().stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public FilmResponseDto getFilm(Long id) {
        Film film = filmStorage.getFilm(id);
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmResponseDto create(FilmDto filmDto) {
        Mpa mpa = mpaStorage.getMpa(filmDto.getMpa().getId());

        System.out.println("filmDto = " + filmDto);
        Film film = FilmMapper.mapToFilm(filmDto, mpa);

        System.out.println("film = " + film);
        Film created = filmStorage.save(film);
        return FilmMapper.mapToFilmDto(created);
    }

    public FilmResponseDto update(FilmDto film) {

        Film origin = filmStorage.getFilm(film.getId());
        if (film.getName() != null)
            origin.setName(film.getName());

        if (film.getDuration() != null)
            origin.setDuration(film.getDuration());

        if (film.getDescription() != null)
            origin.setDescription(film.getDescription());

        if (film.getReleaseDate() != null)
            origin.setReleaseDate(film.getReleaseDate());

        if (film.getMpa() != null)
            origin.setMpa(mpaStorage.getMpa(film.getMpa().getId()));

        if (film.getGenres() != null)
            origin.setGenres(film.getGenres().stream()
                    .map(genreDto -> new Genre(genreDto.getId(), genreDto.getName()))
                    .collect(Collectors.toList()));

        Film updated = filmStorage.update(origin);
        return FilmMapper.mapToFilmDto(updated);
    }
}
