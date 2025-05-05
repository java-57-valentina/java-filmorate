package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.AdvanceInfo;
import ru.yandex.practicum.filmorate.model.BasicInfo;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<GenreDto> getAll() {
        return genreService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GenreDto getGenre(@PathVariable Integer id) {
        return genreService.getGenre(id);
    }

    @PostMapping
    public GenreDto create(@Validated (BasicInfo.class) @RequestBody GenreDto genreDto) {
        GenreDto created = genreService.create(genreDto);
        log.info("Genre id:{} was added: {}", created.getId(), created);
        return created;
    }

    @PutMapping
    public GenreDto update(@Validated (AdvanceInfo.class) @RequestBody GenreDto genreDto) {
        GenreDto updated = genreService.update(genreDto);
        log.info("Genre id:{} was updated: {}", updated.getId(), updated);
        return updated;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Integer id) {
        genreService.delete(id);
        log.info("Genre id:{} was deleted", id);
    }
}
