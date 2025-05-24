package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorStorage directorStorage;

    public Collection<Director> getAll() {
        return directorStorage.getAll();
    }

    public Director getDirector(Integer id) {
        return directorStorage.getDirector(id);
    }

    public void removeDirector(Integer id) {
        directorStorage.removeDirector(id);
    }

    public Director create(Director director) {
        return directorStorage.create(director);
    }

    public Director update(Director director) {
        return directorStorage.update(director);
    }
}
