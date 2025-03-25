package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    User createUser(String login, String name, String email, LocalDate bd) {
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
                LocalDate.parse("2005-01-01"));
        assertDoesNotThrow(user::validate);
    }

    @Test
    void validate_null_name() {
        User user = createUser("login", null, "email@e.com",
                LocalDate.parse("2005-01-01"));
        assertDoesNotThrow(user::validate);
    }

    @Test
    void validate_null_login() {
        User user = createUser(null, "name", "email@e.com",
                LocalDate.parse("2005-01-01"));
        assertThrows(ValidationException.class, user::validate);
    }

    @Test
    void validate_bad_login() {
        User user = createUser("the login", "name", "email@e.com",
                LocalDate.parse("2005-01-01"));
        assertThrows(ValidationException.class, user::validate);
    }

    @Test
    void validate_empty_login() {
        User user = createUser("", "name", "email@e.com",
                LocalDate.parse("2005-01-01"));
        assertThrows(ValidationException.class, user::validate);
    }

    @Test
    void validate_null_email() {
        User user = createUser("login", "name", null,
                LocalDate.parse("2005-01-01"));
        assertThrows(ValidationException.class, user::validate);
    }

    @Test
    void validate_empty_email() {
        User user = createUser("login", "name", "",
                LocalDate.parse("2005-01-01"));
        assertThrows(ValidationException.class, user::validate);
    }

    @Test
    void validate_bad_email_1() {
        User user = createUser("login", "name", "@dot.com",
                LocalDate.parse("2005-01-01"));
        assertThrows(ValidationException.class, user::validate);
    }

    @Test
    void validate_bad_email_2() {
        User user = createUser("login", "name", "test@",
                LocalDate.parse("2005-01-01"));
        assertThrows(ValidationException.class, user::validate);
    }

    @Test
    void validate_bad_email_3() {
        User user = createUser("login", "name", "test.dot.com",
                LocalDate.parse("2005-01-01"));
        assertThrows(ValidationException.class, user::validate);
    }

    @Test
    void validate_null_birthdate() {
        User user = createUser("login", null, "email@e.com", null);
        assertThrows(ValidationException.class, user::validate);
    }

    @Test
    void validate_bad_birthdate() {
        LocalDate localDate = LocalDate.now().plus(1, ChronoUnit.DAYS);
        User user = createUser("login", null, "email@e.com", localDate);
        assertThrows(ValidationException.class, user::validate);
    }


    @Test
    void testEquals() {
        LocalDate now = LocalDate.now();
        User user1 = createUser("login", "name", "email@dot.com", now);
        User user2 = createUser("login", "name", "email@dot.com", now);

        assertEquals(user1, user2);
    }
}