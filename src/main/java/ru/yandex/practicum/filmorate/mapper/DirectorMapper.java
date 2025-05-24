package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.model.Director;


@UtilityClass
public class DirectorMapper {

    public Director mapToDirector(DirectorDto directorDto) {
        return Director.builder().id(directorDto.getId()).build();
    }
}
