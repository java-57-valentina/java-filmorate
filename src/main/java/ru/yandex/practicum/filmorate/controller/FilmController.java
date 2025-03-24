package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

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
    public Film create(@RequestBody Film film) {
        try {
            film.validate();
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw e;
        }

        final long id = getNextId();
        film.setId(id);
        films.put(id, film);
        log.info("Film id:{} was added: {}", id, film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        if (film.getId() == null)
            throw new ConditionsNotMetException("Id должен быть указан");

        if (!films.containsKey(film.getId()))
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");

        try {
            film.validate();
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw e;
        }

        films.put(film.getId(), film);
        log.info("Film id:{} was updated: {}", film.getId(), film);
        return film;
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
