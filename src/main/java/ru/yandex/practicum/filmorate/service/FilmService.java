package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

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

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getFilm(Long id) {
        return filmStorage.getFilm(id);
    }

    public Film create(Film film) {
        film.clearLikes();
        return filmStorage.create(film);
    }

    public Film update(Film film) {

        Film origin = filmStorage.getFilm(film.getId());
        if (film.getName() != null)
            origin.setName(film.getName());

        if (film.getDuration() != null)
            origin.setDuration(film.getDuration());

        if (film.getDescription() != null)
            origin.setDescription(film.getDescription());

        if (film.getReleaseDate() != null)
            origin.setReleaseDate(film.getReleaseDate());

        return filmStorage.update(film);
    }
}
