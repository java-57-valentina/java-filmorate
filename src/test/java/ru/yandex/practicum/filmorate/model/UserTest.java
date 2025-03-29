package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
@Import(ValidationAutoConfiguration.class)

class UserTest {
    @Test
    void testEquals() {
        LocalDate now = LocalDate.now();
        User user1 = new User(1L, "login", "name", "email@dot.com", now);
        User user2 = new User(1L, "login", "name", "email@dot.com", now);

        assertEquals(user1, user2);
    }
}