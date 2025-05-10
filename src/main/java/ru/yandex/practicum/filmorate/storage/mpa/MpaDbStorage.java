package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class MpaDbStorage extends BaseStorage<Mpa> implements MpaStorage {

    private static final String SQL_SELECT_ALL =  "SELECT * FROM mpa ORDER BY id";
    private static final String SQL_SELECT_ONE = "SELECT * FROM mpa WHERE id = ?";

    public MpaDbStorage(JdbcTemplate jdbcTemplate, MpaRowMapper rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    @Override
    public Collection<Mpa> getAll() {
        return getMany(SQL_SELECT_ALL);
    }

    @Override
    public Mpa getMpa(Short id) {
        Optional<Mpa> one = getOne(SQL_SELECT_ONE, id);
        if (one.isEmpty())
            throw new NotFoundException("Mpa id:" + id + " not found");
        return one.get();
    }
}
