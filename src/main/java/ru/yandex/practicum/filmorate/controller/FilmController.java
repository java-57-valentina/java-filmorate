package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.AdvanceInfo;
import ru.yandex.practicum.filmorate.model.BasicInfo;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Validated (BasicInfo.class) @RequestBody Film film) {
        validate(film);

        final long id = getNextId();
        film.setId(id);
        films.put(id, film);
        log.info("Film id:{} was added: {}", id, film);
        return film;
    }

    @PutMapping
    public Film update(@Validated (AdvanceInfo.class) @RequestBody Film film) {

        if (!films.containsKey(film.getId()))
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");

        Film origin = films.get(film.getId());
        if (film.getName() != null)
            origin.setName(film.getName());

        if (film.getDuration() != null)
            origin.setDuration(film.getDuration());

        if (film.getDescription() != null)
            origin.setDescription(film.getDescription());

        if (film.getReleaseDate() != null)
            origin.setReleaseDate(film.getReleaseDate());

        log.info("Film id:{} was updated: {}", origin.getId(), origin);
        return origin;
    }

    private static final LocalDate FIRST_FILM_RELEASE_DATE
            = LocalDate.of(1895, 12, 28);

    private void validate(Film film) throws ValidationException {
        validateReleaseDate(film.getReleaseDate());
    }

    private void validateReleaseDate(@NotNull LocalDate date) {
        if (date.isBefore(FIRST_FILM_RELEASE_DATE))
            throw new ValidationException("Дата релиза не может быть ранее "
                    + FIRST_FILM_RELEASE_DATE.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
