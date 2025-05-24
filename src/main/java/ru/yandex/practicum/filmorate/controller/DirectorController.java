package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;

    @PostMapping
    public Director create(@RequestBody Director director) {
        log.info("Получен запрос на создание director: {}", director);
        Director created = directorService.create(director);
        log.info("Director id: {} was added: {}", created.getId(), created);
        return created;
    }

    @GetMapping
    public Collection<Director> getAll() {
        log.info("Получен запрос на получение списка всех режиссеров");
        return directorService.getAll();
    }

    @GetMapping("/{id}")
    public Director getDirector(@PathVariable Integer id) {
        log.info("Получен запрос на получение режиссера по id: {}", id);
        return directorService.getDirector(id);
    }

    @PutMapping
    public Director update(@RequestBody Director director) {
        log.info("Получен запрос на обновление director: {}", director);
        return directorService.update(director);
    }

    @DeleteMapping("/{id}")
    public void removeDirector(@PathVariable Integer id) {
        log.info("Получен запрос на удаление режиссера по id: {}", id);
        directorService.removeDirector(id);
    }
}
