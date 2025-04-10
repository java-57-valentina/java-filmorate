package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> getAll();

    Film getFilm(Long id);

    Film create(Film film);

    Film update(Film film);

    Collection<Film> getTop(int count);
}
