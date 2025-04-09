package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EmailAlreadyTakenException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public  User create(User user) {
        if (emails.contains(user.getEmail()))
            throw new EmailAlreadyTakenException(user.getEmail());

        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());

        user.setId(getNextId());
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        return user;
    }

    @Override
    public User update(User user) {
        User origin = getUser(user.getId());

        String oldEmail = origin.getEmail();
        String newEmail = user.getEmail();

        if (newEmail != null
                && !newEmail.equals(oldEmail)
                && emails.contains(newEmail)) {
            throw new EmailAlreadyTakenException(newEmail);
        }

        users.put(user.getId(), user);
        emails.remove(oldEmail);
        emails.add(newEmail);

        log.info("User id:{} was updated: {}", user.getId(), user);
        return user;
    }

    @Override
    public User getUser(Long id) {
        User user = users.get(id);
        if (user == null)
            throw new NotFoundException("User id:" + id + " not found");
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
