package ru.yandex.practicum.filmorate.storage.like;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;


@Slf4j
@Repository
public class LikeDbStorage extends BaseStorage<Void> implements LikeStorage {

    private static final String SQL_CHECK_LIKE_EXISTS =
            "SELECT COUNT(*) > 0 FROM likes WHERE film_id = ? AND user_id = ?";
    private static final String SQL_ADD_LIKE = """
            INSERT INTO likes(film_id, user_id)
            VALUES(?, ?)
            """;
    private static final String SQL_REMOVE_LIKE = """
            DELETE FROM likes
            WHERE film_id = ? AND user_id = ?
            """;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, (rs, rowNum) -> null);
    }

    @Override
    public void save(Long filmId, Long userId) {
        jdbcTemplate.update(SQL_ADD_LIKE, filmId, userId);
    }

    @Override
    public void delete(Long filmId, Long userId) {
        jdbcTemplate.update(SQL_REMOVE_LIKE, filmId, userId);
    }

    @Override
    public boolean existsLike(Long filmId, Long userId) {
        return exists(SQL_CHECK_LIKE_EXISTS, filmId, userId);
    }
}
