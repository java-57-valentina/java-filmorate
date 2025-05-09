package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmTest {

    @Test
    void testEquals() {
        LocalDate now = LocalDate.now();
        Film film1 = new Film();
        film1.setId(1L);
        film1.setName("name");
        film1.setDescription("text");
        film1.setReleaseDate(now);
        film1.setDuration(10);

        Film film2 = new Film();
        film2.setId(1L);
        film2.setName("name");
        film2.setDescription("text");
        film2.setReleaseDate(now);
        film2.setDuration(10);

        assertEquals(film1, film2);
    }
}