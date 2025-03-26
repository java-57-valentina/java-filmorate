package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    Long id;

    @NotNull
    @NotBlank
    @Email
    String email;

    @NotNull
    @NotBlank
    @Pattern(regexp = "[^ ]*")
    String login;

    String name;

    @NotNull
    LocalDate birthday;

    public void validate() throws ValidationException {
        validateBirthday();
    }

    private void validateBirthday() {
        if (birthday.isAfter(LocalDate.now()))
            throw new ValidationException("Дата рождения не может быть в будущем");
    }
}

