package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public interface FilmStorage {
    static final LocalDate FIRST_FILM_RELEASE_DATE
            = LocalDate.of(1895, 12, 28);

    Collection<Film> getAll();

    Film getFilm(Long id);

    Film create(Film film);

    Film update(Film film);

    default void validate(Film film) throws ValidationException {
        validateReleaseDate(film.getReleaseDate());
    }

    default void validateReleaseDate(LocalDate date) {
        if (date.isBefore(FIRST_FILM_RELEASE_DATE))
            throw new ValidationException("Дата релиза не может быть ранее "
                    + FIRST_FILM_RELEASE_DATE.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}
