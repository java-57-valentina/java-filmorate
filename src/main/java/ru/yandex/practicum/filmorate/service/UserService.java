package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.exception.EmailAlreadyTakenException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    public Collection<UserResponseDto> getAll() {
        return userStorage.getAll().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserResponseDto getUser(Long id) {
        User found = userStorage.getUser(id);
        return UserMapper.mapToUserDto(found);
    }

    public UserResponseDto create(UserCreateDto userCreateDto) {
        if (userStorage.isEmailUsed(userCreateDto.getEmail())) {
            throw new EmailAlreadyTakenException(userCreateDto.getEmail());
        }

        User userToCreate = UserMapper.mapToUser(userCreateDto);
        User created = userStorage.save(userToCreate);
        if (created == null)
            throw new IllegalStateException("Failed to save data for new user");
        log.info("User was created: {}", created);
        return UserMapper.mapToUserDto(created);
    }

    public UserResponseDto update(UserUpdateDto user) {
        User origin = userStorage.getUser(user.getId());

        if (user.getEmail() != null && !user.getEmail().equals(origin.getEmail())) {
            if (userStorage.isEmailUsed(user.getEmail()))
                throw new EmailAlreadyTakenException(user.getEmail());

            origin.setEmail(user.getEmail());
        }

        if (user.getBirthday() != null)
            origin.setBirthday(user.getBirthday());

        if (user.getName() != null)
            origin.setName(user.getName());

        if (user.getLogin() != null)
            origin.setLogin(user.getLogin());

        User updated = userStorage.update(origin);
        if (updated == null)
            throw new IllegalStateException("Не удалось сохранить данные для пользователя");

        return UserMapper.mapToUserDto(updated);
    }

    public void addFriend(Long id, Long friendId) {
        if (id.equals(friendId))
            throw new ValidationException(id + " = " + friendId);

        userStorage.addFriend(id, friendId);
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
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toSet());
    }

    public Collection<UserResponseDto> getFriends(Long id) {

        Collection<UserResponseDto> friends = userStorage.getFriendsOfUser(id).stream()
                .map(UserMapper::mapToUserDto)
                .toList();
        log.debug("User id:{} has {} friends", id, friends.size());
        return friends;
    }


    public Collection<FilmResponseDto> getRecommendations(Long id) {
        Set<Long> filmIds = userStorage.getRecommendationsIds(id);
        log.debug("Recommendations Ids for user {}: {}", id, filmIds);
        return filmStorage.getFilmSet(filmIds)
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    public void delete(Long id) {
        userStorage.deleteUser(id);
    }
}
