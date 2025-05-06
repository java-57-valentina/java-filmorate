package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;

import java.util.Collection;
import java.util.Optional;

@Primary
@Repository("filmDbStorage")
public class FilmDbStorage extends BaseStorage<Film> implements FilmStorage {

    private static final String SQL_SELECT_ALL = """
            SELECT f.*, r.name as mpa
            FROM films f
            LEFT JOIN ratings r ON r.id = f.rating_id
            """;

    private static final String SQL_SELECT_ONE =
            SQL_SELECT_ALL + " WHERE f.id = ?";

    private static final String SQL_INSERT = """
            INSERT INTO films (title, description, duration, release_date, rating_id)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String SQL_UPDATE_FILM = """
            UPDATE films
            SET title = ?, description = ?, duration= ?,  release_date = ?
            WHERE id = ?
            """;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmRowMapper rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    @Override
    public Collection<Film> getAll() {
        return getMany(SQL_SELECT_ALL);
    }

    @Override
    public Film getFilm(Long id) {
        Optional<Film> one = getOne(SQL_SELECT_ONE, id);
        if (one.isEmpty())
            throw new NotFoundException("Film id:" + id + " not found");
        return one.get();
    }

    @Override
    public Collection<Film> getTop(int count) {
        return null;
    }

    @Override
    public Film create(Film film) {

        Long id = insertAndReturnId(SQL_INSERT,
                Long.class,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId()
        );

        if (id == null)
            return null;

        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film film) {
        int updated = super.update(SQL_UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getId());
        if (updated > 0)
            return film;
        return null;
    }
}
