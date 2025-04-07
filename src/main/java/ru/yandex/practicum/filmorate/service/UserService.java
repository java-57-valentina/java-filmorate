package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public void addFriend(Long id, Long friendId) {
        if (id.equals(friendId))
            throw new ValidationException(id + " = " + friendId);
        User user1 = userStorage.getUser(id);
        User user2 = userStorage.getUser(friendId);

        user1.addFriend(friendId);
        user2.addFriend(id);

        log.info("User id:{} has added friend id:{}", id, friendId);
    }

    public void removeFriend(Long id, Long friendId) {

        User user1 = userStorage.getUser(id);
        User user2 = userStorage.getUser(friendId);

        user1.removeFriend(friendId);
        user2.removeFriend(id);

        log.info("User id:{} has removed friend id:{}", id, friendId);
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
