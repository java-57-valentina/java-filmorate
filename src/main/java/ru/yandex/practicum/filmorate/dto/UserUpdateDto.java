package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserUpdateDto {
    @NotNull
    Long id;

    @Email(message = "Неверный формат email")
    String email;

    @Pattern(regexp = "[^ ]*")
    String login;

    String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past
    LocalDate birthday;
}
