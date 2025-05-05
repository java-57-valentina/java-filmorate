package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.AdvanceInfo;
import ru.yandex.practicum.filmorate.model.BasicInfo;

@Data
public class GenreDto {
    @NotNull(groups = AdvanceInfo.class)
    Integer id;
    @NotBlank(groups = {BasicInfo.class, AdvanceInfo.class})
    String name;
}
