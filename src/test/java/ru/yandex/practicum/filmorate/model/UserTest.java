package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    User createUser(String login, String name, String email, Instant bd) {
        User user = new User();
        user.setName(name);
        user.setLogin(login);
        user.setEmail(email);
        user.setBirthday(bd);
        return user;
    }


    @Test
    void validate_ok() {
        User user = createUser("login", "name", "email@e.com",
                Instant.parse("2005-01-01T00:00:00.63Z"));
        assertDoesNotThrow(user::validate);
    }

    @Test
    void validate_null_name() {
        User user = createUser("login", null, "email@e.com",
                Instant.parse("2005-01-01T00:00:00.63Z"));
        assertDoesNotThrow(user::validate);
    }

    @Test
    void validate_null_login() {
        User user = createUser(null, "name", "email@e.com",
                Instant.parse("2005-01-01T00:00:00.63Z"));
        assertThrows(ValidationException.class, user::validate);
    }

    @Test
    void validate_bad_login() {
        User user = createUser("the login", "name", "email@e.com",
                Instant.parse("2005-01-01T00:00:00.63Z"));
        assertThrows(ValidationException.class, user::validate);
    }

    @Test
    void validate_empty_login() {
        User user = createUser("", "name", "email@e.com",
                Instant.parse("2005-01-01T00:00:00.63Z"));
        assertThrows(ValidationException.class, user::validate);
    }

    @Test
    void validate_null_email() {
        User user = createUser("login", "name", null,
                Instant.parse("2005-01-01T00:00:00.63Z"));
        assertThrows(ValidationException.class, user::validate);
    }

    @Test
    void validate_empty_email() {
        User user = createUser("login", "name", "",
                Instant.parse("2005-01-01T00:00:00.63Z"));
        assertThrows(ValidationException.class, user::validate);
    }

    @Test
    void validate_bad_email_1() {
        User user = createUser("login", "name", "@dot.com",
                Instant.parse("2005-01-01T00:00:00.63Z"));
        assertThrows(ValidationException.class, user::validate);
    }

    @Test
    void validate_bad_email_2() {
        User user = createUser("login", "name", "test@",
                Instant.parse("2005-01-01T00:00:00.63Z"));
        assertThrows(ValidationException.class, user::validate);
    }

    @Test
    void validate_bad_email_3() {
        User user = createUser("login", "name", "test.dot.com",
                Instant.parse("2005-01-01T00:00:00.63Z"));
        assertThrows(ValidationException.class, user::validate);
    }

    @Test
    void validate_null_birthdate() {
        User user = createUser("login", null, "email@e.com", null);
        assertThrows(ValidationException.class, user::validate);
    }

    @Test
    void validate_bad_birthdate() {
        Instant instant = Instant.now().plus(1, ChronoUnit.DAYS);
        User user = createUser("login", null, "email@e.com", instant);
        assertThrows(ValidationException.class, user::validate);
    }


    @Test
    void testEquals() {
        Instant instant = Instant.now();
        User user1 = createUser("login", "name", "email@dot.com", instant);
        User user2 = createUser("login", "name", "email@dot.com", instant);

        assertEquals(user1, user2);
    }
}