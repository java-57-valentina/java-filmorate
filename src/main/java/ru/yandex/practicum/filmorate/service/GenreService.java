package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDbStorage genreStorage;
    private final GenreMapper genreMapper;

    public List<GenreDto> getAll() {
        return genreStorage.findAll()
                .stream()
                .map(genreMapper::mapToGenreDto)
                .collect(Collectors.toList());
    }

    public GenreDto getGenre(Integer id) {
        Genre found = genreStorage.getGenre(id);
        return genreMapper.mapToGenreDto(found);
    }

    public GenreDto create(GenreDto genreDto) {
        Genre genreToCreate = genreMapper.mapToGenre(genreDto);
        Genre created = genreStorage.create(genreToCreate);
        if (created == null)
            throw new IllegalStateException("Failed to save data for new genre");
        log.info("Genre was created: {}", created);
        return genreMapper.mapToGenreDto(created);
    }

    public GenreDto update(GenreDto genre) {
        Genre origin = genreStorage.getGenre(genre.getId());
        origin.setName(genre.getName());
        Genre updated = genreStorage.update(origin);
        if (updated == null)
            throw new IllegalStateException("Failed to update genre " + genre);
        return genreMapper.mapToGenreDto(updated);
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean delete(Integer id) {
        return genreStorage.delete(id);
    }
}
