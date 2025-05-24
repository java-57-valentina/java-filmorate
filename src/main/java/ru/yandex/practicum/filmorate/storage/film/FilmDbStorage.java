package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;
import ru.yandex.practicum.filmorate.storage.director.DirectorRowMapper;

import java.util.*;
import java.util.stream.Collectors;

@Repository("filmDbStorage")
public class FilmDbStorage extends BaseStorage<Film> implements FilmStorage {


    private static final String SQL_SELECT_ALL = """
            SELECT
                f.*,
                m.name AS mpa_name,
                (
                    SELECT STRING_AGG(CONCAT(g.id, ':', g.name), '|' ORDER BY g.id)
                    FROM film_genre fg
                    JOIN genres g ON fg.genre_id = g.id
                    WHERE fg.film_id = f.id
                ) AS genres_data,
                (
                    SELECT STRING_AGG(CONCAT(d.id, ':', d.name), '|' ORDER BY d.id)
                    FROM film_director fd
                    JOIN directors d ON fd.director_id = d.id
                    WHERE fd.film_id = f.id
                ) AS directors_data
            FROM films f
            LEFT JOIN mpa m ON m.id = f.mpa_id;
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
                ) AS genres_data,
                (
                    SELECT STRING_AGG(CONCAT(d.id, ':', d.name), '|' ORDER BY d.id)
                    FROM film_director fd
                    JOIN directors d ON fd.director_id = d.id
                    WHERE fd.film_id = f.id
                ) AS directors_data
            FROM films f
            LEFT JOIN mpa m ON m.id = f.mpa_id
            WHERE f.id = ?;
            """;

    private static final String SQL_SELECT_SET = """
            SELECT
                f.*,
                m.name AS mpa_name,
                (
                    SELECT STRING_AGG(CONCAT(g.id, ':', g.name), '|' ORDER BY g.id)
                    FROM film_genre fg
                    JOIN genres g ON fg.genre_id = g.id
                    WHERE fg.film_id = f.id
                ) AS genres_data,
                (
                        SELECT STRING_AGG(CONCAT(d.id, ':', d.name), '|' ORDER BY d.id)
                        FROM film_director fd
                        JOIN directors d ON fd.director_id = d.id
                        WHERE fd.film_id = f.id
                    ) AS directors_data
            FROM films f
            LEFT JOIN mpa m ON m.id = f.mpa_id
            WHERE f.id IN (%s)
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

    private static final String DELETE_FILM_DIRECTORS = "DELETE FROM film_director WHERE film_id =?;";

    private static final String INSERT_FILM_GENRE = """
            INSERT INTO film_genre(film_id, genre_id)
            VALUES (?, ?)
            """;

    private static final String INSERT_FILM_DIRECTOR = "INSERT INTO film_director (film_id, director_id) VALUES (?,?);";

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
                            ) AS genres_data,
            (
                    SELECT STRING_AGG(CONCAT(d.id, ':', d.name), '|' ORDER BY d.id)
                    FROM film_director fd
                    JOIN directors d ON fd.director_id = d.id
                    WHERE fd.film_id = f.id
               ) AS directors_data
                       FROM films f
                       LEFT JOIN mpa m ON m.id = f.mpa_id
                        LEFT JOIN likes l ON f.id = l.film_id
                       GROUP BY
                           f.id, f.title, f.description, f.duration, f.release_date, f.mpa_id, m.name
                        ORDER BY likes_count DESC
                        LIMIT ?;
            """;

    private static final String SQL_SELECT_POPULAR_BY_DIRECTOR = """
            SELECT
                f.*,
                m.name AS mpa_name,
                (
                    SELECT STRING_AGG(CONCAT(g.id, ':', g.name), '|' ORDER BY g.id)
                    FROM film_genre fg
                    JOIN genres g ON fg.genre_id = g.id
                    WHERE fg.film_id = f.id
                ) AS genres_data,
                (
                    SELECT STRING_AGG(CONCAT(d.id, ':', d.name), '|' ORDER BY d.id)
                    FROM film_director fd
                    JOIN directors d ON fd.director_id = d.id
                    WHERE fd.film_id = f.id
                ) AS directors_data,
                COUNT(l.film_id) AS likes_count
            FROM films f
            LEFT JOIN mpa m ON m.id = f.mpa_id
            JOIN film_director fd ON f.id = fd.film_id
            JOIN directors d ON fd.director_id = d.id
            LEFT JOIN likes l ON f.id = l.film_id
            WHERE d.id = ?
            GROUP BY f.id, m.name, genres_data, directors_data
            ORDER BY LIKES_COUNT DESC;
            """;

    private static final String SQL_SELECT_BY_DIRECTOR_SORTED_BY_YEAR = """
            SELECT
                f.*,
                m.name AS mpa_name,
                (
                    SELECT STRING_AGG(CONCAT(g.id, ':', g.name), '|' ORDER BY g.id)
                    FROM film_genre fg
                    JOIN genres g ON fg.genre_id = g.id
                    WHERE fg.film_id = f.id
                ) AS genres_data,
                (
                    SELECT STRING_AGG(CONCAT(d.id, ':', d.name), '|' ORDER BY d.id)
                    FROM film_director fd
                    JOIN directors d ON fd.director_id = d.id
                    WHERE fd.film_id = f.id
                ) AS directors_data
            FROM films f
            LEFT JOIN mpa m ON m.id = f.mpa_id
            JOIN film_director fd ON f.id = fd.film_id
            JOIN directors d ON fd.director_id = d.id
            WHERE d.id = ?
            ORDER BY f.RELEASE_DATE;
            """;

    private static final String SQL_GET_DIRECTOR_BY_ID = "SELECT * FROM directors WHERE id=?;";

    private final DirectorRowMapper directorRowMapper;

    private static final String SQL_DELETE_FILM = "DELETE FROM films WHERE id = ?";

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmRowMapper rowMapper, DirectorRowMapper directorRowMapper) {
        super(jdbcTemplate, rowMapper);
        this.directorRowMapper = directorRowMapper;
    }


    @Override
    public Collection<Film> getAll() {
        Collection<Film> many = getMany(SQL_SELECT_ALL);
        return many;
    }

    @Override
    public Film getFilm(Long id) {
        Optional<Film> one = getOne(SQL_SELECT_ONE, id);
        return one.orElseThrow(() -> new NotFoundException("Film id:" + id + " not found"));
    }

    @Override
    public Collection<Film> getFilmSet(Set<Long> ids) {
        String sqlParams = String.join(",", Collections.nCopies(ids.size(), "?"));

        return super.getMany(String.format(SQL_SELECT_SET, sqlParams), ids.toArray());
    }

    @Override
    public void checkFilmExists(Long id) {
        boolean exists = exists(SQL_CHECK_FILM_EXISTS, id);
        if (!exists)
            throw new NotFoundException("Film id:" + id + " not found");
    }

    @Override
    public void deleteFilm(Long id) {
        update(SQL_DELETE_FILM, id);
    }

    @Override
    public Film save(Film film) {

        Set<Short> genreIds = film.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());


        checkGenresExist(genreIds);

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
        saveFilmsDirectors(film);
        /* берем актуальный объект из базы, т.к. в получаемом объекте
         * информация о жанрах/mpa может быть неполной (допускается
         * наличие только их id, без названий) */
        return getFilm(id);
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
        saveFilmsDirectors(film);
        /* берем актуальный объект из базы, т.к. в получаемом объекте
         * информация о жанрах/mpa может быть неполной (допускается
         * наличие только их id, без названий) */
        return getFilm(film.getId());
    }

    @Override
    public Collection<Film> getTop(int count) {
        return getMany(SQL_SELECT_POPULAR, count);
    }

    @Override
    public Collection<Film> getSortedFilmsByDirector(Integer directorId, String sortBy) {
        switch (sortBy) {
            case "year":
                return getMany(SQL_SELECT_BY_DIRECTOR_SORTED_BY_YEAR, directorId);
            case "likes":
                return getMany(SQL_SELECT_POPULAR_BY_DIRECTOR, directorId);
        }
        throw new ValidationException(String.format("Wrong sort option: %s", sortBy));
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

    private void saveFilmsDirectors(Film film) {
        Set<Integer> directorsIds = film.getDirectors().stream()
                .map(Director::getId)
                .collect(Collectors.toSet());

        update(DELETE_FILM_DIRECTORS, film.getId());

        if (directorsIds.isEmpty()) return;

        jdbcTemplate.batchUpdate(INSERT_FILM_DIRECTOR, directorsIds, directorsIds.size(),
                (ps, directorsId) -> {
                    ps.setLong(1, film.getId());
                    ps.setInt(2, directorsId);
                });
    }

    private void checkGenresExist(Set<Short> ids) throws NotFoundException {
        if (ids.isEmpty()) {
            return;
        }

        String sql = "SELECT t.id FROM (VALUES " +
                ids.stream()
                        .map(id -> "(?)")
                        .collect(Collectors.joining(",")) +
                ") AS t(id) WHERE t.id NOT IN (SELECT id FROM genres)";

        List<Short> invalidIds = jdbcTemplate.queryForList(sql, Short.class, ids.toArray());
        if (!invalidIds.isEmpty())
            throw new NotFoundException("Genres are invalid: " + invalidIds);
    }

    private Director getDirector(Integer id) {
        Optional<Director> one = Optional.ofNullable(
                jdbcTemplate.queryForObject(SQL_GET_DIRECTOR_BY_ID, directorRowMapper, id));
        return one.orElseThrow(() -> new NotFoundException("Director id:" + id + " not found"));
    }
}
