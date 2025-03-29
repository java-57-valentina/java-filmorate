package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.AdvanceInfo;
import ru.yandex.practicum.filmorate.model.BasicInfo;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    Collection<User> getAll() {
        return users.values();
    }

    @PostMapping
    public  User create(@Validated(BasicInfo.class) @RequestBody User user) {
        user.setId(getNextId());
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());

        users.put(user.getId(), user);
        log.info("User id:{} was added: {}", user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Validated(AdvanceInfo.class) @RequestBody User user) {

        if (!users.containsKey(user.getId()))
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");

        User origin = users.get(user.getId());

        if (user.getEmail() != null)
            origin.setEmail(user.getEmail());

        if (user.getBirthday() != null)
            origin.setBirthday(user.getBirthday());

        if (user.getName() != null)
            origin.setName(user.getName());

        if (user.getLogin() != null)
            origin.setLogin(user.getLogin());

        log.info("User id:{} was updated: {}", origin.getId(), origin);
        return origin;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
