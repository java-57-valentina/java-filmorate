package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Data
public class Film {
    public static final int MAX_DESCRIPTION_SIZE = 200;
    private static final LocalDate FIRST_FILM_RELEASE_DATE
            = LocalDate.of(1895, 12, 28);

    Long id;
    String name;
    String description;
    LocalDate releaseDate;
    Duration duration;

    public void validate() throws ValidationException {
        validateName();
        validateDescription();
        validateReleaseDate();
        validateDuration();
    }

    private void validateDuration() {
        if (duration == null)
            throw new ValidationException("Продолжительность должна быть положительным числом");

        if (duration.isZero() || duration.isNegative())
            throw new ValidationException("Продолжительность должна быть положительным числом");
    }

    private void validateReleaseDate() {
        if (releaseDate == null)
            throw new ValidationException("Дата релиза не задана");

        if (releaseDate.isBefore(FIRST_FILM_RELEASE_DATE))
            throw new ValidationException("Дата релиза не может быть ранее "
                    + FIRST_FILM_RELEASE_DATE.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    private void validateName() {
        if (name == null)
            throw new ValidationException("Название не может быть пустым");

        if (name.isBlank())
            throw new ValidationException("Название не может быть пустым");
    }

    private void validateDescription() {
        if (description != null && description.length() > MAX_DESCRIPTION_SIZE)
            throw new ValidationException("Максимальная длина описания - " + MAX_DESCRIPTION_SIZE + " символов");
    }
}
