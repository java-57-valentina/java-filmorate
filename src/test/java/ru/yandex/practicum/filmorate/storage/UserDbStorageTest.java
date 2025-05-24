package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserRowMapper;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase
@Import({ UserDbStorage.class, UserRowMapper.class})
public class UserDbStorageTest {

    @Autowired
    private UserDbStorage storage;
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
    void delete() {
        User created = storage.save(user);
        storage.deleteUser(created.getId());
        String message = "User id:" + created.getId() + " not found";

        assertThatThrownBy(() -> storage.checkUserExists(created.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(message);
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
}