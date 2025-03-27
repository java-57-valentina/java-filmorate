package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


@Data
@AllArgsConstructor
public class Film {
    public static final int MAX_DESCRIPTION_SIZE = 200;

    @NotNull (groups = AdvanceInfo.class)
    private Long id;

    @NotBlank (groups = BasicInfo.class)
    private String name;

    @Size(max = MAX_DESCRIPTION_SIZE, message = "Максимальная длина описания - 200 символов", groups = BasicInfo.class)
    private String description;

    @NotNull (groups = BasicInfo.class)
    private LocalDate releaseDate;

    @NotNull (groups = BasicInfo.class)
    @Min(value = 1, message = "Продолжительность должна быть положительным числом", groups = BasicInfo.class)
    private Integer duration;
}
