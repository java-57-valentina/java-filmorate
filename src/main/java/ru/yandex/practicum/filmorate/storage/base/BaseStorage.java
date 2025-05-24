package ru.yandex.practicum.filmorate.storage.base;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
public abstract class BaseStorage<T> {
    protected final JdbcTemplate jdbcTemplate;
    protected final RowMapper<T> rowMapper;

    protected boolean exists(String sql, Object... args) {
        Boolean b = jdbcTemplate.queryForObject(sql, Boolean.class, args);
        return Boolean.TRUE.equals(b);
    }

    protected Optional<T> getOne(String sql, Object... args) {
        try {
            T result = jdbcTemplate.queryForObject(sql, rowMapper, args);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    protected Collection<T> getMany(String sql, Object... args) {
        return jdbcTemplate.query(sql, rowMapper, args);
    }

    protected int delete(String sql, Object... args) {
        return jdbcTemplate.update(sql, args);
    }

    protected int update(String sql, Object... args) {
        return jdbcTemplate.update(sql, args);
    }

    protected <K> K insertAndReturnId(String sql, Class<K> clazz, Object... args) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            return ps;
        }, keyHolder);
        return keyHolder.getKeyAs(clazz);
    }

    protected Map<String, Object> insertAndReturnKeys(String sql, Object... args) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            return ps;
        }, keyHolder);
        return keyHolder.getKeys();
    }
}
