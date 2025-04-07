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
        User user1 = new User();
        user1.setId(1L);
        user1.setLogin("login");
        user1.setName("user");
        user1.setEmail("email@gmail.com");
        user1.setBirthday(LocalDate.ofYearDay(2020, 1));
        user1.addFriend(1L);

        User user2 = new User();
        user2.setId(1L);
        user2.setLogin("login");
        user2.setName("user");
        user2.setEmail("email@gmail.com");
        user2.setBirthday(LocalDate.ofYearDay(2020, 1));
        user2.addFriend(1L);

        assertEquals(user1, user2);
    }
}