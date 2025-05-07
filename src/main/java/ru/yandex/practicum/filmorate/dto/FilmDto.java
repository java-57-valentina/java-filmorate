package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collection;


@Data
public class FilmDto {
    public static final int MAX_DESCRIPTION_SIZE = 200;

    @NotNull(groups = UpdateInfo.class)
    Long id;

    @NotNull(groups = CreateInfo.class)
    @Pattern(regexp = "^(?!\\s*$).+",
            message = "Не должно быть пустой строкой",
            groups = {CreateInfo.class, UpdateInfo.class})
    private String name;

    @Pattern(regexp = "^(?!\\s*$).+",
            message = "Не должно быть пустой строкой",
            groups = {CreateInfo.class, UpdateInfo.class})
    @Size(max = MAX_DESCRIPTION_SIZE,
            message = "Максимальная длина описания - 200 символов",
            groups = {CreateInfo.class, UpdateInfo.class})
    private String description;

    @NotNull(groups = CreateInfo.class)
    @Min(value = 1,
            message = "Продолжительность должна быть положительным числом",
            groups = {CreateInfo.class, UpdateInfo.class})
    private Integer duration;

    @Past
    @NotNull(groups = CreateInfo.class)
    private LocalDate releaseDate;

    @NotNull(groups = CreateInfo.class)
    private MpaDto mpa;

    private Collection<GenreDto> genres = null;
}
