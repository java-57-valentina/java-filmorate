package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {

    Film createFilm(String name, String description, LocalDate date, Duration duration) {
        Film film = new Film();
        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(date);
        film.setDuration(duration);
        return film;
    }

    @Test
    void validate_ok() {
        Film film = createFilm("name", "desc", LocalDate.now(), Duration.ofMinutes(120));
        assertDoesNotThrow(film::validate);
    }

    @Test
    void validate_null_name() {
        Film film = createFilm(null, "desc", LocalDate.now(), Duration.ofMinutes(120));
        assertThrows(ValidationException.class, film::validate);
    }

    @Test
    void validate_empty_name() {
        Film film = createFilm(" ", "desc", LocalDate.now(), Duration.ofMinutes(120));
        assertThrows(ValidationException.class, film::validate);
    }

    @Test
    void validate_valid_description() {
        String text = "a".repeat(Film.MAX_DESCRIPTION_SIZE);
        Film film = createFilm("name", text, LocalDate.now(), Duration.ofMinutes(120));
        assertDoesNotThrow(film::validate);
    }

    @Test
    void validate_bad_description() {
        String text = "a".repeat(Film.MAX_DESCRIPTION_SIZE + 1);
        Film film = createFilm("name", text, LocalDate.now(), Duration.ofMinutes(120));
        assertThrows(ValidationException.class, film::validate);
    }

    @Test
    void validate_null_release() {
        Film film = createFilm("name", "text", null, Duration.ofMinutes(120));
        assertThrows(ValidationException.class, film::validate);
    }

    @Test
    void validate_bad_release() {
        LocalDate localDate = LocalDate.parse("1895-12-27");
        Film film = createFilm("name", "text", localDate, Duration.ofMinutes(120));
        assertThrows(ValidationException.class, film::validate);
    }

    @Test
    void validate_null_duration() {
        Film film = createFilm("name", "text", LocalDate.now(), null);
        assertThrows(ValidationException.class, film::validate);
    }

    @Test
    void validate_bad_duration() {
        Film film = createFilm("name", "text", LocalDate.now(), Duration.ofMinutes(0));
        assertThrows(ValidationException.class, film::validate);
    }


    @Test
    void testEquals() {
        LocalDate now = LocalDate.now();
        Film film1 = createFilm("name", "text", now, Duration.ofMinutes(10));
        Film film2 = createFilm("name", "text", now, Duration.ofMinutes(10));
        assertEquals(film1, film2);
    }
}