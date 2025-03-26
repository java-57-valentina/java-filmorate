package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {

    private static Validator validator;

    @BeforeAll
    static void beforeAll() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void validate_ok() {
        Film film = new Film(null, "name", "desc", LocalDate.now(), 120);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertDoesNotThrow(film::validate);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validate_null_name() {
        Film film = new Film(null, null, "desc", LocalDate.now(), 120);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

    @Test
    void validate_empty_name() {
        Film film = new Film(null, " ", "desc", LocalDate.now(), 120);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

    @Test
    void validate_valid_description() {
        String text = "a".repeat(Film.MAX_DESCRIPTION_SIZE);
        Film film = new Film(null, "name", text, LocalDate.now(), 120);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty());
    }

    @Test
    void validate_bad_description() {
        String text = "a".repeat(Film.MAX_DESCRIPTION_SIZE + 1);
        Film film = new Film(null, "name", text, LocalDate.now(), 120);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

    @Test
    void validate_null_release() {
        Film film = new Film(null, "name", "text", null, 120);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

    @Test
    void validate_bad_release() {
        LocalDate localDate = LocalDate.parse("1895-12-27");
        Film film = new Film(null, "name", "text", localDate, 120);

        assertThrows(ValidationException.class, film::validate);
    }

    @Test
    void validate_null_duration() {
        Film film = new Film(null, "name", "text", LocalDate.now(), 0);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

    @Test
    void validate_bad_duration() {
        Film film = new Film(null, "name", "text", LocalDate.now(), -1);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }


    @Test
    void testEquals() {
        LocalDate now = LocalDate.now();
        Film film1 = new Film(1L, "name", "text", now, 10);
        Film film2 = new Film(1L, "name", "text", now, 10);
        assertEquals(film1, film2);
    }
}