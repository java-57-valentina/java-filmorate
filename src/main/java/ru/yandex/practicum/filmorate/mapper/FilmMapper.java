package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilmMapper {

    private final MpaMapper mpaMapper;
    private final GenreMapper genreMapper;

    public Film mapToFilm(FilmDto request, Mpa mpaDto) {
        Film film = new Film();
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setReleaseDate(request.getReleaseDate());
        film.setDuration(request.getDuration());
        film.setMpa(new Mpa(request.getMpa().getId(), mpaDto.getName()));

        if (request.getGenres() != null) {
            film.setGenres(
                    request.getGenres().stream()
                            .map(genreMapper::mapToGenre)
                            .collect(Collectors.toSet()));
        }

        return film;
    }

    public FilmResponseDto mapToFilmDto(Film film) {
        MpaDto mpa = mpaMapper.mapToDto(film.getMpa());

        return new FilmResponseDto(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                mpa,
                film.getGenres().stream()
                        .map(genreMapper::mapToGenreDto)
                        .collect(Collectors.toList())
        );
    }
}
