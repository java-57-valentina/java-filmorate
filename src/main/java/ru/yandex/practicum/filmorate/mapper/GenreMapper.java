package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

@Component
public class GenreMapper {

    public GenreDto mapToGenreDto(Short id) {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(id);
        return genreDto;
    }

    public GenreDto mapToGenreDto(Genre genre) {
        return mapToGenreDto(genre.getId());
    }
}
