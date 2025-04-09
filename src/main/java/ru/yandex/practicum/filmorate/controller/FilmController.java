package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.AdvanceInfo;
import ru.yandex.practicum.filmorate.model.BasicInfo;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;
    private static final LocalDate FIRST_FILM_RELEASE_DATE
            = LocalDate.of(1895, 12, 28);

    @GetMapping("/films")
    public Collection<Film> getAll() {
        return filmService.getAll();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable Long id) {
        return filmService.getFilm(id);
    }

    @PostMapping("/films")
    public Film create(@Validated (BasicInfo.class) @RequestBody Film film) {
        validate(film);
        Film created = filmService.create(film);
        log.info("Film id:{} was added: {}", created.getId(), created);
        return created;
    }

    @PutMapping("/films")
    public Film update(@Validated (AdvanceInfo.class) @RequestBody Film film) {
        validate(film);
        Film updated = filmService.update(film);
        log.info("Film id:{} was updated: {}", updated.getId(), updated);
        return updated;
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film like(@PathVariable Long id, @PathVariable Long userId) {
        Film film = filmService.addLike(id, userId);
        log.info("User id:{} has liked film id:{}", userId, id);
        return film;
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film unlike(@PathVariable Long id, @PathVariable Long userId) {
        Film film = filmService.removeLike(id, userId);
        log.info("User id:{} has removed like from film id:{}", userId, id);
        return film;
    }

    @GetMapping("/films/popular")
    public Collection<Film> getTopFilms(
            @RequestParam(defaultValue = "10") int count) {
        if (count <= 0)
            throw new ValidationException("count > 0");
        return filmService.getTop(count);
    }

    private void validate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(FIRST_FILM_RELEASE_DATE))
            throw new ValidationException("The release date cannot be earlier "
                    + FIRST_FILM_RELEASE_DATE.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}
