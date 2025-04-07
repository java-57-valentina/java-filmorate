package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
            throw new NotFoundException("Film " + id + " not found");
        return film;
    }

    @Override
    public Film create(Film film) {
        validate(film);

        final long id = getNextId();
        film.setId(id);
        films.put(id, film);
        log.info("Film id:{} was added: {}", id, film);
        return film;
    }

    @Override
    public Film update(Film film) {

        if (!films.containsKey(film.getId()))
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");

        Film origin = films.get(film.getId());
        if (film.getName() != null)
            origin.setName(film.getName());

        if (film.getDuration() != null)
            origin.setDuration(film.getDuration());

        if (film.getDescription() != null)
            origin.setDescription(film.getDescription());

        if (film.getReleaseDate() != null)
            origin.setReleaseDate(film.getReleaseDate());

        log.info("Film id:{} was updated: {}", origin.getId(), origin);
        return origin;
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
