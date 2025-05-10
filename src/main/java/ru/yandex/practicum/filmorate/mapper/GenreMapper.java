package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

@UtilityClass
public class GenreMapper {

    public GenreDto mapToGenreDto(Genre genre) {
        GenreDto responseDto = new GenreDto();
        responseDto.setName(genre.getName());
        responseDto.setId(genre.getId());
        return responseDto;
    }

    public Genre mapToGenre(GenreDto genreDto) {
        Genre genre = new Genre();
        genre.setId(genreDto.getId());
        genre.setName(genreDto.getName());
        return genre;
    }
}
