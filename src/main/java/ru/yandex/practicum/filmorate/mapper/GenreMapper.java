package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

@Component
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
