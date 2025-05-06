package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmCreateDto;
import ru.yandex.practicum.filmorate.dto.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.RatingDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

@Component
@RequiredArgsConstructor
public class FilmMapper {

    final RatingMapper ratingMapper;

    public Film mapToFilm(FilmCreateDto request, Rating ratingDto) {
        Film film = new Film();
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setReleaseDate(request.getReleaseDate());
        film.setDuration(request.getDuration());
        film.setMpa(new Rating(request.getMpa().getId(), ratingDto.getName()));
        return film;
    }

    public FilmResponseDto mapToFilmDto(Film film) {
        RatingDto mpa = ratingMapper.mapToDto(film.getMpa());
        return new FilmResponseDto(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                mpa
        );
    }
}
