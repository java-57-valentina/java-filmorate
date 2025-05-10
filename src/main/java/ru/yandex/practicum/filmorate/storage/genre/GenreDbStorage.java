package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class GenreDbStorage extends BaseStorage<Genre> implements GenreStorage {

    private static final String SQL_SELECT_ALL =  "SELECT * FROM genres ORDER BY id";
    private static final String SQL_SELECT_ONE = "SELECT * FROM genres WHERE id = ?";

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
}
