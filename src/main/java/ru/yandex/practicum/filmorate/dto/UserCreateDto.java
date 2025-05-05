package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserCreateDto {

    @Email(message = "Неверный формат email")
    @NotBlank
    String email;

    @NotBlank
    @Pattern(regexp = "[^ ]*")
    String login;

    String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    @Past
    LocalDate birthday;
}
