package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.UserResponseDto;
import ru.yandex.practicum.filmorate.model.User;

@UtilityClass
public class UserMapper {
    public User mapToUser(UserCreateDto request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setLogin(request.getLogin());
        user.setBirthday(request.getBirthday());
        user.setName(request.getName());

        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());

        return user;
    }

    public UserResponseDto mapToUserDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday()
        );
    }
}
