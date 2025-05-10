package ru.yandex.practicum.filmorate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String login;
    private String name;
    private String email;
    private LocalDate birthday;
}
