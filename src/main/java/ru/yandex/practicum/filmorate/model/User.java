package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

@Data
public class User {
    Long id;
    String email;
    String login;
    String name;
    LocalDate birthday;

    public void validate() throws ValidationException {
        validateLogin();
        validateEmail();
        validateBirthday();
    }

    private void validateBirthday() {
        if (birthday == null)
            throw new ValidationException("Дата рождения не задан");
        if (birthday.isAfter(LocalDate.now()))
            throw new ValidationException("Дата рождения не может быть в будущем");
    }

    private void validateLogin() {
        if (login == null)
            throw new ValidationException("Логин не может быть пустым");

        if (login.isBlank())
            throw new ValidationException("Логин не может быть пустым");

        if (login.contains(" "))
            throw new ValidationException("Логин не может содержать пробелы");
    }

    private void validateEmail() {
        if (email == null)
            throw new ValidationException("E-mail не может быть пустым");

        if (email.isBlank())
            throw new ValidationException("E-mail не может быть пустым");

        int index = email.indexOf("@");
        if (index <= 0 || index == email.length() - 1)
            throw new ValidationException("E-mail должен содержать символ '@'");
    }
}
