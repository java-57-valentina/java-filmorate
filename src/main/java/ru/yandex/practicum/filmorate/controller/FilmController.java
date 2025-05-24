package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.CreateInfo;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.UpdateInfo;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikeService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;
    private final LikeService likeService;

    private static final LocalDate FIRST_FILM_RELEASE_DATE
            = LocalDate.of(1895, 12, 28);


    @GetMapping
    public Collection<FilmResponseDto> getAll() {
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public FilmResponseDto getFilm(@PathVariable Long id) {
        return filmService.getFilm(id);
    }

    @PostMapping
    public FilmResponseDto create(@Validated(CreateInfo.class) @RequestBody FilmDto film) {
        validate(film);
        FilmResponseDto created = filmService.create(film);
        log.info("Film id:{} was added: {}", created.getId(), created);
        return created;
    }

    @PutMapping
    public FilmResponseDto update(@Validated(UpdateInfo.class) @RequestBody FilmDto film) {
        validate(film);
        FilmResponseDto updated = filmService.update(film);
        log.info("Film id:{} was updated: {}", updated.getId(), updated);
        return updated;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        filmService.delete(id);
        log.info("Film id:{} was removed", id);
    }

    @PutMapping("/{id}/like/{userId}")
    public FilmResponseDto like(@PathVariable Long id, @PathVariable Long userId) {
        likeService.addLike(id, userId);
        log.info("User id:{} has liked film id:{}", userId, id);
        return filmService.getFilm(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public FilmResponseDto unlike(@PathVariable Long id, @PathVariable Long userId) {
        likeService.removeLike(id, userId);
        log.info("User id:{} has removed like from film id:{}", userId, id);
        return filmService.getFilm(id);
    }

    @GetMapping("/popular")
    public Collection<FilmResponseDto> getTopFilms(
            @RequestParam(required = false) Integer count,
            @RequestParam(required = false) Short genreId,
            @RequestParam(required = false) Short year) {
        if (count != null && count <= 0)
            throw new ValidationException("count > 0");
        return filmService.getTop(count, genreId, year);
    }

    private void validate(FilmDto film) throws ValidationException {
        if (film.getReleaseDate() == null)
            return;

        if (film.getReleaseDate().isBefore(FIRST_FILM_RELEASE_DATE))
            throw new ValidationException("The release date cannot be earlier "
                    + FIRST_FILM_RELEASE_DATE.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}
