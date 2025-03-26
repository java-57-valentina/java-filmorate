package ru.yandex.practicum.filmorate.model;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@Import(ValidationAutoConfiguration.class)

class UserTest {

    static Validator validator;

    @BeforeAll
    static void beforeAll() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void validate_ok() {
        User user = new User(null, "email@dot.com", "login", "Alex",
                LocalDate.parse("2005-01-01"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validate_null_name() {
        User user = new User(null, "email@dot.com", "login", null,
                LocalDate.parse("2005-01-01"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }


    @Test
    void validate_null_login() {
        User user = new User(null, "email@dot.com", null, "test",
                LocalDate.parse("2005-01-01"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validate_bad_login() {
        User user = new User(null, "email@dot.com", "the login", "test",
                LocalDate.parse("2005-01-01"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validate_empty_login() {
        User user = new User(null, "email@dot.com", "", "test",
                LocalDate.parse("2005-01-01"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validate_null_email() {
        User user = new User(null, null, null, "test",
                LocalDate.parse("2005-01-01"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validate_empty_email() {
        User user = new User(null, "", "login", "Alex",
                LocalDate.parse("2005-01-01"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validate_bad_email_1() {
        User user = new User(null, "email @dot.com", "login", "Alex",
                LocalDate.parse("2005-01-01"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validate_bad_email_2() {
        User user = new User(null, "email@", "login", "Alex",
                LocalDate.parse("2005-01-01"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validate_bad_email_3() {
        User user = new User(null, "email@.com", "login", "Alex",
                LocalDate.parse("2005-01-01"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validate_null_birthday() {
        User user = new User(null, "email@dot.com", "login", "Alex", null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validate_bad_birthdate() {
        User user = new User(null, "email@dot.com", "login", "Alex",
                LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, user::validate);
    }


    @Test
    void testEquals() {
        LocalDate now = LocalDate.now();
        User user1 = new User(1L, "login", "name", "email@dot.com", now);
        User user2 = new User(1L, "login", "name", "email@dot.com", now);

        assertEquals(user1, user2);
    }
}