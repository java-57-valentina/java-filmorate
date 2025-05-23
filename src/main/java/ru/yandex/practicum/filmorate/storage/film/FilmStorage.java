package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Set;

public interface FilmStorage {

    Collection<Film> getAll();

    Film getFilm(Long id);

    Collection<Film> getFilmSet(Set<Long> ids);

    Film save(Film film);

    Film update(Film film);

    Collection<Film> getTop(int count);

    void checkFilmExists(Long filmId);
}
