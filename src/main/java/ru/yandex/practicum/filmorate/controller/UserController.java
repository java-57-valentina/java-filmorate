package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
    public  User create(@Valid @RequestBody User user) {
        user.validate();
        user.setId(getNextId());
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());

        users.put(user.getId(), user);
        log.info("User id:{} was added: {}", user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {

        if (user.getId() == null)
            throw new ConditionsNotMetException("Id должен быть указан");

        if (!users.containsKey(user.getId()))
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");

        user.validate();

        users.put(user.getId(), user);
        log.info("User id:{} was updated: {}", user.getId(), user);
        return user;
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
