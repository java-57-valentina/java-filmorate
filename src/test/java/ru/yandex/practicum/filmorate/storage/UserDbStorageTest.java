package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserRowMapper;
import ru.yandex.practicum.filmorate.storage.user.UsersLikesExtractor;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserRowMapper.class, UsersLikesExtractor.class,
LikeDbStorage.class, FilmDbStorage.class, FilmRowMapper.class})
public class UserDbStorageTest {

    private final UserDbStorage storage;
    private final LikeDbStorage likeDbStorage;
    private final FilmDbStorage filmDbStorage;
    private final User user = new User("user", "Name Surname", "test@gmail.com", LocalDate.of(1991, 1, 1));

    @Test
    public void createUser() {
        int count = storage.getAll().size();

        User created = storage.save(user);

        Assertions.assertNotNull(created.getId());
        Assertions.assertEquals(count + 1, storage.getAll().size());
    }

    @Test
    public void getUser() {
        User created = storage.save(user);
        User found = storage.getUser(created.getId());

        Assertions.assertEquals(user.getName(), found.getName());
        Assertions.assertEquals(user.getLogin(), found.getLogin());
        Assertions.assertEquals(user.getEmail(), found.getEmail());
        Assertions.assertEquals(user.getBirthday(), found.getBirthday());
    }

    @Test
    void getNonExisting() {
        Long nonExistentId = 100500L;
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> storage.getUser(nonExistentId)
        );
        assertThat(exception.getMessage()).contains("User id:100500 not found");
    }

    @Test
    public void testUpdateUser() {
        User created = storage.save(user);

        created.setLogin("new_login");
        created.setName("new_name");
        created.setEmail("new_email@gmail.com");

        storage.update(created);
        User found = storage.getUser(created.getId());

        Assertions.assertEquals("new_login", found.getLogin());
        Assertions.assertEquals("new_name", found.getName());
        Assertions.assertEquals("new_email@gmail.com", found.getEmail());
    }

    @Test
    public void testGetRecommendations() {
        User user1 = new User("john123", "John", "john@ya.ru",
                LocalDate.of(2000, 1, 1));
        User user2 = new User("vasya123", "Vasya", "vasya@ya.ru",
                LocalDate.of(2010, 2, 2));
        User user3 = new User("petya123", "Petr", "petya@ya.ru",
                LocalDate.of(2010, 2, 2));

        long user1Id = storage.save(user1).getId();
        long user2Id = storage.save(user2).getId();
        long user3Id = storage.save(user3).getId();

        Film film1 = new Film("Film1", "", 100,
                LocalDate.of(2000, 1, 1), new Mpa((short) 1,""));
        Film film2 = new Film("Film2", "", 100,
                LocalDate.of(2000, 1, 1), new Mpa((short) 1,""));
        Film film3 = new Film("Film3", "", 100,
                LocalDate.of(2000, 1, 1), new Mpa((short) 1,""));
        Film film4 = new Film("Film4", "", 100,
                LocalDate.of(2000, 1, 1), new Mpa((short) 1,""));
        Film film5 = new Film("Film5", "", 100,
                LocalDate.of(2000, 1, 1), new Mpa((short) 1,""));

        long film1Id = filmDbStorage.save(film1).getId();
        long film2Id = filmDbStorage.save(film2).getId();
        filmDbStorage.save(film3);
        long film4Id = filmDbStorage.save(film4).getId();
        long film5Id = filmDbStorage.save(film5).getId();

        likeDbStorage.save(film1Id, user1Id);
        likeDbStorage.save(film2Id, user1Id);
        likeDbStorage.save(film1Id, user2Id);
        likeDbStorage.save(film4Id, user2Id);
        likeDbStorage.save(film1Id, user3Id);
        likeDbStorage.save(film5Id, user2Id);

        Set<Long> filmIds = storage.getRecommendationsIds(user1Id);

        assertNotNull(filmIds);
        assertEquals(2, filmIds.size());
        assertTrue(filmIds.contains(film4Id));
        assertTrue(filmIds.contains(film5Id));
    }
}