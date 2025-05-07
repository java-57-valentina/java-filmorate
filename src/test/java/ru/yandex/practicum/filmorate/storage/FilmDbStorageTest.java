package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRowMapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase
@Import({ FilmDbStorage.class, GenreDbStorage.class, FilmRowMapper.class, GenreRowMapper.class, MpaRowMapper.class})
public class FilmDbStorageTest {

    @Autowired
    private FilmDbStorage storage;
     private static final Film film1 = new Film("Film1 Name", "Boring film", 120, LocalDate.of(1991, 1, 1), new Mpa((short) 1, "f"));
     private static final Film film2 = new Film("Film2 Name", "Interesting film", 120, LocalDate.of(1991, 1, 1), new Mpa((short) 1,"2"));


    @Test
    void create() {
        storage.save(film1);
        assertThatThrownBy(() -> storage.getFilm(100500L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Film id:100500 not found");
    }

    @Test
    void update() {
        Film created = storage.save(film1);
        created.setName("new name");
        created.setDescription("new desc");
        created.setDuration(20);
        created.setReleaseDate(LocalDate.of(2020,2,2));
        created.setMpa(new Mpa((short) 2, "PG"));
        created.setGenres(List.of(
                new Genre((short) 2, "x"),
                new Genre((short) 3, "x")));

        // Act
        Film updated = storage.update(created);
        Film found = storage.getFilm(created.getId());
        System.out.println("found.getGenres() = " + found.getGenres());

        // Assert
        assertThat(updated).isNotNull();
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("new name");
        assertThat(found.getDescription()).isEqualTo("new desc");
        assertThat(found.getReleaseDate()).isEqualTo(created.getReleaseDate());
        assertThat(found.getMpa()).extracting(Mpa::getId).isEqualTo((short)2);
        assertThat(found.getGenres()).extracting(Genre::getId).contains((short) 2);
        assertThat(found.getGenres()).extracting(Genre::getId).contains((short) 3);
    }

    @Test
    void getNonExisting() {
        Long nonExistentId = 100500L;
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> storage.getFilm(nonExistentId)
        );
        assertThat(exception.getMessage()).contains("Film id:100500 not found");
    }

    @Test
    void getAll() {
        storage.save(film1);
        storage.save(film2);

        Collection<Film> films = storage.getAll();
        assertThat(films).hasSize(2);
    }
}