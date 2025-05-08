package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDbStorage genreStorage;
    private final GenreMapper genreMapper;

    public Collection<GenreDto> getAll() {
        return genreStorage.getAll()
                .stream()
                .map(genreMapper::mapToGenreDto)
                .collect(Collectors.toList());
    }

    public GenreDto getGenre(Short id) {
        Genre found = genreStorage.getGenre(id);
        return genreMapper.mapToGenreDto(found);
    }
}
