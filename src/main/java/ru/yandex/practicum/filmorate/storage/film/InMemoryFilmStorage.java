package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film getFilm(Long id) {
        Film film = films.get(id);
        if (film == null)
            throw new NotFoundException("Film id:" + id + " not found");
        return film;
    }

    @Override
    public Film create(Film film) {
        final long id = getNextId();
        film.setId(id);
        films.put(id, film);
        log.info("Film id:{} was added: {}", id, film);
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        log.info("Film id:{} was updated: {}", film.getId(), film);
        return film;
    }

    @Override
    public Collection<Film> getTop(int count) {
        Comparator<? super Film> comparator = Comparator.comparingInt(f -> f.getLikes().size());

        return films.values().stream()
                .sorted(comparator.reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
