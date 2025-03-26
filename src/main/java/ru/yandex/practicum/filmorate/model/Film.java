package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Data
@AllArgsConstructor
public class Film {
    public static final int MAX_DESCRIPTION_SIZE = 200;
    private static final LocalDate FIRST_FILM_RELEASE_DATE
            = LocalDate.of(1895, 12, 28);

    Long id;

    @NotNull
    @NotBlank
    String name;

    @Size(max = MAX_DESCRIPTION_SIZE, message = "Максимальная длина описания - 200 символов")
    String description;

    @NotNull
    LocalDate releaseDate;

    @NotNull
    @Min(value = 1, message = "Продолжительность должна быть положительным числом")
    Integer duration;

    public void validate() throws ValidationException {
        validateReleaseDate();
    }

    private void validateReleaseDate() {
        if (releaseDate == null)
            throw new ValidationException("Дата релиза не задана");

        if (releaseDate.isBefore(FIRST_FILM_RELEASE_DATE))
            throw new ValidationException("Дата релиза не может быть ранее "
                    + FIRST_FILM_RELEASE_DATE.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}
