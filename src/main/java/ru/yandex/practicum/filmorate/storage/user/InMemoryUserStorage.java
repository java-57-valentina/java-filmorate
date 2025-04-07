package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public  User create(User user) {
        user.setId(getNextId());
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());

        users.put(user.getId(), user);
        log.info("User id:{} was added: {}", user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {

        User origin = users.get(user.getId());
        if (origin == null)
            throw new NotFoundException("User id = " + user.getId() + " not found");

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

    @Override
    public User getUser(Long id) {
        User user = users.get(id);
        if (user == null)
            throw new NotFoundException("User " + id + " not found");
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
