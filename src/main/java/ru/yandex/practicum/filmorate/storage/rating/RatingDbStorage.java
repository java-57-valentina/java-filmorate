package ru.yandex.practicum.filmorate.storage.rating;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class RatingDbStorage extends BaseStorage<Rating> implements RatingStorage {

    private static final String SQL_SELECT_ALL =  "SELECT * FROM ratings ORDER BY id";
    private static final String SQL_SELECT_ONE = "SELECT * FROM ratings WHERE id = ?";

    public RatingDbStorage(JdbcTemplate jdbcTemplate, RatingRowMapper rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    @Override
    public Collection<Rating> findAll() {
        return getMany(SQL_SELECT_ALL);
    }

    @Override
    public Rating getRating(Short id) {
        Optional<Rating> one = getOne(SQL_SELECT_ONE, id);
        if (one.isEmpty())
            throw new NotFoundException("Rating id:" + id + " not found");
        return one.get();
    }
}
