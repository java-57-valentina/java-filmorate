package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Primary
@Repository("filmDbStorage")
public class FilmDbStorage extends BaseStorage<Film> implements FilmStorage {
    private final GenreStorage genreStorage;

    private static final String SQL_SELECT_ALL = """
            SELECT
                f.*,
                m.name AS mpa_name,
                (
                    SELECT STRING_AGG(CONCAT(g.id, ':', g.name), '|' ORDER BY g.id)
                    FROM film_genre fg
                    JOIN genres g ON fg.genre_id = g.id
                    WHERE fg.film_id = f.id
                ) AS genres_data
            FROM films f
            LEFT JOIN mpa m ON m.id = f.mpa_id
            ORDER BY f.id;
            """;

    private static final String SQL_SELECT_ONE = """
            SELECT
                f.*,
                m.name AS mpa_name,
                (
                    SELECT STRING_AGG(CONCAT(g.id, ':', g.name), '|' ORDER BY g.id)
                    FROM film_genre fg
                    JOIN genres g ON fg.genre_id = g.id
                    WHERE fg.film_id = f.id
                ) AS genres_data
            FROM films f
            LEFT JOIN mpa m ON m.id = f.mpa_id
            WHERE f.id = ?
            """;

    private static final String SQL_CHECK_FILM_EXISTS =
            "SELECT COUNT(*) > 0 FROM films WHERE id = ?";
    private static final String SQL_INSERT = """
            INSERT INTO films (title, description, duration, release_date, mpa_id)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String SQL_UPDATE_FILM = """
            UPDATE films
            SET title = ?, description = ?, duration= ?,  release_date = ?, mpa_id = ?
            WHERE id = ?
            """;
    private static final String DELETE_FILM_GENRES = """
            DELETE FROM film_genre
            WHERE film_id = ?
            """;

    private static final String INSERT_FILM_GENRE = """
            INSERT INTO film_genre(film_id, genre_id)
            VALUES (?, ?)
            """;

    private static final String SQL_SELECT_POPULAR = """
            SELECT
                f.*,
                m.name AS mpa_name,
                COUNT(l.film_id) as likes_count,
                (
                    SELECT STRING_AGG(CONCAT(g.id, ':', g.name), '|' ORDER BY g.id)
                    FROM film_genre fg
                    JOIN genres g ON fg.genre_id = g.id
                    WHERE fg.film_id = f.id
                ) AS genres_data
            FROM films f
            LEFT JOIN mpa m ON m.id = f.mpa_id
            LEFT JOIN likes l ON f.id = l.film_id
            GROUP BY
               f.id, f.title, f.description, f.duration, f.release_date, f.mpa_id, m.name
            ORDER BY likes_count DESC
            LIMIT ?;
            """;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmRowMapper rowMapper, GenreStorage genreStorage) {
        super(jdbcTemplate, rowMapper);
        this.genreStorage = genreStorage;
    }

    @Override
    public Collection<Film> getAll() {
        Collection<Film> many = getMany(SQL_SELECT_ALL);

        return many;
    }

    @Override
    public Film getFilm(Long id) {
        Optional<Film> one = getOne(SQL_SELECT_ONE, id);
        Film film = one.orElseThrow(() -> new NotFoundException("Film id:" + id + " not found"));

        Collection<Genre> genres = genreStorage.getFilmGenres(id);
        film.setGenres(genres);

        return film;
    }

    @Override
    public void checkFilmExists(Long id) {
        boolean exists = exists(SQL_CHECK_FILM_EXISTS, id);
        if (!exists)
            throw  new NotFoundException("Film id:" + id + " not found");
    }

    @Override
    public Film save(Film film) {

        Set<Short> genreIds = film.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        genreStorage.checkAllExists(genreIds);

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
        saveFilmGenres(film);
        film.setGenres(genreStorage.getFilmGenres(id));
        return film;
    }

    @Override
    public Film update(Film film) {
        super.update(SQL_UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());

        saveFilmGenres(film);
        film.setGenres(genreStorage.getFilmGenres(film.getId()));
        return film;
    }

    @Override
    public Collection<Film> getTop(int count) {
        return getMany(SQL_SELECT_POPULAR, count);
    }

    private void saveFilmGenres(Film film) {
        final Collection<Genre> genres = film.getGenres();

        Set<Short> genreIds = genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        update(DELETE_FILM_GENRES, film.getId());

        if (genreIds.isEmpty())
            return;

        jdbcTemplate.batchUpdate(INSERT_FILM_GENRE, genreIds, genreIds.size(),
                (ps, genreId) -> {
                    ps.setLong(1, film.getId());
                    ps.setInt(2, genreId);
                });
    }
}
