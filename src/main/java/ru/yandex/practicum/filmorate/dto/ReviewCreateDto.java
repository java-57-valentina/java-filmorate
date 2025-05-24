package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewCreateDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long filmId;

    @NotBlank
    private String content;

    @NotNull
    private Boolean isPositive;
}
