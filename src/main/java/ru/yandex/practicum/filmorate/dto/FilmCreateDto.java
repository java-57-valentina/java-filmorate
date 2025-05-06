package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FilmCreateDto {
    public static final int MAX_DESCRIPTION_SIZE = 200;

    @NotBlank
    private String name;

    @NotBlank
    @Size(max = MAX_DESCRIPTION_SIZE, message = "Максимальная длина описания - 200 символов")
    private String description;

    @NotNull
    @Min(value = 1, message = "Продолжительность должна быть положительным числом")
    private Integer duration;

    @NotNull
    @Past
    private LocalDate releaseDate;

    @NotNull
    private RatingDto mpa;
}
