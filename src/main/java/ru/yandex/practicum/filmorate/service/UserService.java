package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public void addFriend(Long id1, Long id2) {
        if (id1.equals(id2))
            throw new ConditionsNotMetException(id1 + " = " + id2);
        User user1 = userStorage.getUser(id1);
        User user2 = userStorage.getUser(id2);

        user1.addFriend(id2);
        user2.addFriend(id1);
    }

    public void removeFriend(Long id1, Long id2) {
        if (id1.equals(id2))
            throw new ConditionsNotMetException(id1 + " = " + id2);

        User user1 = userStorage.getUser(id1);
        User user2 = userStorage.getUser(id2);

        user1.removeFriend(id2);
        user2.removeFriend(id1);
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
