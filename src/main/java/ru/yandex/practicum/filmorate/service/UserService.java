package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.UserResponseDto;
import ru.yandex.practicum.filmorate.dto.UserUpdateDto;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
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
    private final UserMapper userMapper;

    public Collection<UserResponseDto> getAll() {
        return userStorage.getAll().stream()
                .map(userMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserResponseDto getUser(Long id) {
        User found = userStorage.getUser(id);
        return userMapper.mapToUserDto(found);
    }

    public UserResponseDto create(UserCreateDto userCreateDto) {
        User userToCreate = userMapper.mapToUser(userCreateDto);
        User created = userStorage.create(userToCreate);
        if (created == null)
            throw new IllegalStateException("Failed to save data for new user");
        log.info("User was created: {}", created);
        return userMapper.mapToUserDto(created);
    }

    public UserResponseDto update(UserUpdateDto user) {
        User origin = userStorage.getUser(user.getId());

        if (user.getEmail() != null)
            origin.setEmail(user.getEmail());

        if (user.getBirthday() != null)
            origin.setBirthday(user.getBirthday());

        if (user.getName() != null)
            origin.setName(user.getName());

        if (user.getLogin() != null)
            origin.setLogin(user.getLogin());

        User updated = userStorage.update(origin);
        if (updated == null)
            throw new IllegalStateException("Не удалось сохранить данные для пользователя");

        return userMapper.mapToUserDto(updated);
    }

    public void addFriend(Long id, Long friendId) {
        if (id.equals(friendId))
            throw new ValidationException(id + " = " + friendId);

        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId);

        userStorage.addFriend(user, friendId);
    }

    public void removeFriend(Long id, Long friendId) {
        if (id.equals(friendId))
            throw new ValidationException(id + " = " + friendId);

        userStorage.removeFriend(id, friendId);
    }

    public Set<UserResponseDto> getCommonFriends(Long id, Long otherId) {
        if (id.equals(otherId))
            return Set.of();

        Collection<User> commonFriends = userStorage.getCommonFriends(id, otherId);
        return commonFriends.stream()
                .map(userMapper::mapToUserDto)
                .collect(Collectors.toSet());
    }

    public Collection<UserResponseDto> getFriends(Long id) {

        Collection<UserResponseDto> friends = userStorage.getFriendsOfUser(id).stream()
                .map(userMapper::mapToUserDto)
                .toList();
        log.debug("User id:{} has {} friends", id, friends.size());
        return friends;
    }
}
