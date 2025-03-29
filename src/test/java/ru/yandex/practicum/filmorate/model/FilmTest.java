package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmTest {

    @Test
    void testEquals() {
        LocalDate now = LocalDate.now();
        Film film1 = new Film(1L, "name", "text", now, 10);
        Film film2 = new Film(1L, "name", "text", now, 10);
        assertEquals(film1, film2);
    }
}