package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

@Component
public class GenreMapper {

    public GenreDto mapToGenreDto(Genre genre) {
        GenreDto responceDto = new GenreDto();
        responceDto.setName(genre.getName());
        responceDto.setId(genre.getId());
        return responceDto;
    }

    public Genre mapToGenre(GenreDto genreDto) {
        Genre genre = new Genre();
        genre.setId(genreDto.getId());
        genre.setName(genreDto.getName());
        return genre;
    }
}
