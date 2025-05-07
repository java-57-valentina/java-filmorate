package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


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
        film.clearLikes();
        films.put(id, film);
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
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

    @Override
    public void checkFilmExists(Long filmId) {
        if (!films.containsKey(filmId))
            throw new NotFoundException("Film id:" + filmId + " not found");
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
