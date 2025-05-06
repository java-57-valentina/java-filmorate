package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FilmUpdateDto extends FilmDto  {
    @NotNull
    Long id;
}
