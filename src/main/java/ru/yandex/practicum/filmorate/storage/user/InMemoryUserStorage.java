package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyFriendException;
import ru.yandex.practicum.filmorate.exception.EmailAlreadyTakenException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public void checkUserExists(Long id) throws NotFoundException {
        if (!users.containsKey(id))
            throw new NotFoundException("User id:" + id + " not found");
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

        return user;
    }

    @Override
    public User getUser(Long id) {
        User user = users.get(id);
        if (user == null)
            throw new NotFoundException("User id:" + id + " not found");
        return user;
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        User user = getUser(id);
        checkUserExists(friendId);

        if (!user.addFriend(friendId))
            throw new AlreadyFriendException(user.getId(), friendId);
    }

    @Override
    public void removeFriend(Long id, Long friendId) {
        User user = getUser(id);
        user.removeFriend(friendId);
    }

    @Override
    public Collection<User> getFriendsOfUser(Long id) {
        return getUser(id).getFriends().stream()
                .map(this::getUser)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long otherId) {
        Set<Long> friends1 = getUser(id).getFriends();
        Set<Long> friends2 = getUser(otherId).getFriends();

        return friends1.stream()
                .filter(friends2::contains)
                .map(this::getUser)
                .collect(Collectors.toSet());
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
