package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmCreateDto;
import ru.yandex.practicum.filmorate.dto.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.FilmUpdateDto;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final RatingStorage ratingStorage;

    private final FilmMapper filmMapper;

    public Film addLike(Long id, Long userId) {
        Film film = filmStorage.getFilm(id);
        userStorage.getUser(userId);
        film.addLike(userId);
        return film;
    }

    public Film removeLike(Long id, Long userId) {
        Film film = filmStorage.getFilm(id);
        userStorage.getUser(userId);
        film.removeLike(userId);
        return film;
    }

    public Collection<Film> getTop(int count) {
        return filmStorage.getTop(count);
    }

    public Collection<FilmResponseDto> getAll() {
        return filmStorage.getAll().stream()
                .map(filmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public FilmResponseDto getFilm(Long id) {
        Film film = filmStorage.getFilm(id);
        return filmMapper.mapToFilmDto(film);
    }

    public FilmResponseDto create(@Valid FilmCreateDto filmDto) {
        // Check exists
        Rating rating = ratingStorage.getRating(filmDto.getMpa().getId());
        Film film = filmMapper.mapToFilm(filmDto, rating);
        Film created = filmStorage.create(film);
        return filmMapper.mapToFilmDto(created);
    }

    public FilmResponseDto update(@Valid FilmUpdateDto film) {

        Film origin = filmStorage.getFilm(film.getId());
        if (film.getName() != null)
            origin.setName(film.getName());

        if (film.getDuration() != null)
            origin.setDuration(film.getDuration());

        if (film.getDescription() != null)
            origin.setDescription(film.getDescription());

        if (film.getReleaseDate() != null)
            origin.setReleaseDate(film.getReleaseDate());

        Film updated = filmStorage.update(origin);
        return filmMapper.mapToFilmDto(updated);
    }
}
