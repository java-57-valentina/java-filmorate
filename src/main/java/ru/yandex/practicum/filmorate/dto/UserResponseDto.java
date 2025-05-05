package ru.yandex.practicum.filmorate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserResponseDto {
    Long id;
    String login;
    String name;
    String email;
    LocalDate birthday;
}
