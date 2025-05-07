package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Set;

public interface GenreStorage {
    Collection<Genre> getAll();

    Genre getGenre(Short id);

    Collection<Short> checkAllExists(Set<Short> ids);

    Collection<Genre> getFilmGenres(Long id);
}
