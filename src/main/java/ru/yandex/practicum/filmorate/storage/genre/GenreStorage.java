package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Set;

public interface GenreStorage {
    Collection<Genre> getAll();

    Genre getGenre(Short id);

    void checkAllExists(Set<Short> ids) throws NotFoundException;

    Collection<Genre> getFilmGenres(Long id);
}
