package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class GenreDbStorage extends BaseStorage<Genre> implements GenreStorage {

    private static final String SQL_SELECT_ALL =  "SELECT * FROM genres ORDER BY id";
    private static final String SQL_SELECT_ONE = "SELECT * FROM genres WHERE id = ?";

    private static final String SQL_GET_FILM_GENRES = """
            SELECT fg.genre_id as id, g.name
            FROM film_genre fg
            LEFT JOIN genres g ON g.id = fg.genre_id
            WHERE fg.film_id = ?
            """;

    public GenreDbStorage(JdbcTemplate jdbcTemplate, GenreRowMapper rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    @Override
    public Collection<Genre> getAll() {
        return getMany(SQL_SELECT_ALL);
    }

    @Override
    public Genre getGenre(Short id) {
        Optional<Genre> one = getOne(SQL_SELECT_ONE, id);
        if (one.isEmpty())
            throw new NotFoundException("Genre id:" + id + " not found");
        return one.get();
    }

    @Override
    public void checkAllExists(Set<Short> ids) throws NotFoundException {
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

    @Override
    public Collection<Genre> getFilmGenres(Long id) {
        return getMany(SQL_GET_FILM_GENRES, id);
    }
}
