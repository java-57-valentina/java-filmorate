package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Primary
@Repository("filmDbStorage")
public class FilmDbStorage extends BaseStorage<Film> implements FilmStorage {

    final GenreStorage genreStorage;

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
    private static final String DELETE_FILM_GENRES = """
            DELETE FROM film_genre
            WHERE film_id = ?
            """;

    private static final String INSERT_FILM_GENRE = """
            INSERT INTO film_genre(film_id, genre_id)
            VALUES (?, ?)
            """;
    private static final String GET_FILM_GENRES = """
            SELECT genre_id
            FROM film_genre
            WHERE film_id = ?
            """;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmRowMapper rowMapper, GenreStorage genreStorage) {
        super(jdbcTemplate, rowMapper);
        this.genreStorage = genreStorage;
    }

    @Override
    public Collection<Film> getAll() {
        return getMany(SQL_SELECT_ALL);
    }

    @Override
    public Film getFilm(Long id) {
        Optional<Film> one = getOne(SQL_SELECT_ONE, id);
        Film film = one.orElseThrow(() -> new NotFoundException("Film id:" + id + " not found"));

        List<Integer> all = jdbcTemplate.queryForList(
                "SELECT film_id FROM film_genre",
                Integer.class
        );

        List<Short> genres = jdbcTemplate.queryForList(
                GET_FILM_GENRES,
                Short.class,
                id
        );
        System.out.println("genres = " + genres);

        film.setGenres(List.copyOf(genres));
        return film;
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
        saveFilmGenres(film);
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

    private void saveFilmGenres(Film film) {
        final Collection<Short> genres = film.getGenres();

        List<Short> invalidIds = genreStorage.checkAllExists(genres);
        if (!invalidIds.isEmpty())
            throw new NotFoundException("Genres are invalid: " + invalidIds);

        update(DELETE_FILM_GENRES, film.getId());

        if (genres.isEmpty())
            return;

        jdbcTemplate.batchUpdate(INSERT_FILM_GENRE, genres, genres.size(),
                (ps, genreId) -> {
                    ps.setLong(1, film.getId());
                    ps.setInt(2, genreId);
                });
    }
}
