package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User getUser(Long id) {
        return userStorage.getUser(id);
    }

    public User create(User user) {
        user.clearFriends();
        return userStorage.create(user);
    }

    public User update(User user) {
        User origin = userStorage.getUser(user.getId());

        if (user.getEmail() != null)
            origin.setEmail(user.getEmail());

        if (user.getBirthday() != null)
            origin.setBirthday(user.getBirthday());

        if (user.getName() != null)
            origin.setName(user.getName());

        if (user.getLogin() != null)
            origin.setLogin(user.getLogin());

        return userStorage.update(origin);
    }

    public void addFriend(Long id, Long friendId) {
        if (id.equals(friendId))
            throw new ValidationException(id + " = " + friendId);

        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId);

        user.addFriend(friendId);
        friend.addFriend(id);
    }

    public void removeFriend(Long id, Long friendId) {
        User user1 = userStorage.getUser(id);
        User user2 = userStorage.getUser(friendId);

        user1.removeFriend(friendId);
        user2.removeFriend(id);
    }

    public Set<User> getCommonFriends(Long id, Long otherId) {
        if (id.equals(otherId))
            return Set.of();

        Set<Long> friends1 = userStorage.getUser(id).getFriends();
        Set<Long> friends2 = userStorage.getUser(otherId).getFriends();

        return friends1.stream()
                .filter(friends2::contains)
                .map(userStorage::getUser)
                .collect(Collectors.toSet());
    }

    public Collection<User> getFriends(Long id) {
        User user = userStorage.getUser(id);
        return user.getFriends().stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }
}
