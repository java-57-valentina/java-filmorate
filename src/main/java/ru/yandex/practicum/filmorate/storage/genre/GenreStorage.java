package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

public interface GenreStorage {
    Collection<Genre> findAll();

    Genre getGenre(Short id);

    Genre create(Genre genre);

    Genre update(Genre genre);

    boolean delete(Short id);

    List<Short> checkAllExists(List<Short> genres);
}
