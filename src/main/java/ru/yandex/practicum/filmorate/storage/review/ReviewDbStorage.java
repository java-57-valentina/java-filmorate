package ru.yandex.practicum.filmorate.storage.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository("reviewDbStorage")
public class ReviewDbStorage extends BaseStorage<Review> implements ReviewStorage {

    private static final String SQL_SELECT_ALL = """
            SELECT
                r.*,
                    (SELECT COUNT(*) FROM rate_review WHERE review_id = r.id AND is_useful = true) -
                    (SELECT COUNT(*) FROM rate_review WHERE review_id = r.id AND is_useful = false) AS useful
            FROM reviews r
            ORDER BY useful DESC
            LIMIT ?;
            """;

    private static final String SQL_SELECT_BY_FILM = """
            SELECT r.*,
                    (SELECT COUNT(*) FROM rate_review WHERE review_id = r.id AND is_useful = true) -
                    (SELECT COUNT(*) FROM rate_review WHERE review_id = r.id AND is_useful = false) AS useful
            FROM reviews r
            WHERE r.film_id = ?
            ORDER BY useful DESC
            LIMIT ?;
            """;

    private static final String SQL_SELECT_ONE = """
            SELECT
                r.*,
                    (SELECT COUNT(*) FROM rate_review WHERE review_id = r.id AND is_useful = true) -
                    (SELECT COUNT(*) FROM rate_review WHERE review_id = r.id AND is_useful = false) AS useful
            FROM reviews r
            WHERE r.id = ?
            """;

    private static final String SQL_UPDATE_REVIEW = """
            UPDATE reviews
            SET content = ?, is_positive = ?
            WHERE id = ?
            """;

    private static final String SQL_INSERT_REVIEW = """
            INSERT INTO reviews (content, is_positive, user_id, film_id)
            VALUES (?, ?, ?, ?)
            """;
    private static final String SQL_DELETE_REVIEW = """
            DELETE FROM reviews
            WHERE id = ?
            """;

    private static final String SQL_CHECK_REVIEW_EXISTS = """
            SELECT COUNT(*) > 0 FROM reviews WHERE user_id = ? AND film_id = ?;
            """;

    private static final String SQL_CHECK_RATE_REVIEW_EXISTS = """
            SELECT COUNT(*) > 0 FROM rate_review WHERE user_id = ? AND review_id = ?;
            """;

    private static final String SQL_INSERT_RATE_REVIEW = """
            INSERT INTO rate_review (user_id, review_id, is_useful)
            VALUES (?, ?, ?)
            """;

    private static final String SQL_UPDATE_RATE_REVIEW = """
            UPDATE rate_review
            SET is_useful = ?
            WHERE user_id = ? AND review_id = ?;
            """;

    private static final String SQL_DELETE_RATE_REVIEW = """
            DELETE FROM rate_review
            WHERE user_id = ? AND review_id = ? AND is_useful = ?;
            """;

    @Autowired
    public ReviewDbStorage(JdbcTemplate jdbcTemplate, ReviewRowMapper rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    public ReviewDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Review> rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    @Override
    public Collection<Review> getAll(int count) {
        return super.getMany(SQL_SELECT_ALL, count);
    }

    @Override
    public Collection<Review> getByFilm(Long filmId, int count) {
        return super.getMany(SQL_SELECT_BY_FILM, filmId, count);
    }

    @Override
    public Review getReview(Long id) {
        Optional<Review> one = getOne(SQL_SELECT_ONE, id);
        return one.orElseThrow(() -> new NotFoundException("Review", id));
    }

    @Override
    public Review save(Review review) {
        Map<String, Object> keys = super.insertAndReturnKeys(SQL_INSERT_REVIEW,
                review.getContent(),
                review.isPositive(),
                review.getUserId(),
                review.getFilmId());

        if (keys == null)
            return null;

        Long id = (Long)keys.get("id");
        Timestamp timestamp = (Timestamp) keys.get("created_at");
        review.setId(id);
        review.setCreatedAt(timestamp.toLocalDateTime());
        return review;
    }

    @Override
    public boolean delete(Long id) {
        int updated = super.update(SQL_DELETE_REVIEW, id);
        return updated == 1;
    }

    @Override
    public Review update(Review review) {
        super.update(SQL_UPDATE_REVIEW,
                review.getContent(),
                review.isPositive(),
                review.getId());
        return review;
    }

    @Override
    public boolean isReviewExists(Long userId, Long filmId) {
        return exists(SQL_CHECK_REVIEW_EXISTS, userId, filmId);
    }

    @Override
    public boolean isReviewRatedByUser(Long userId, Long reviewId) {
        return exists(SQL_CHECK_RATE_REVIEW_EXISTS, userId, reviewId);
    }

    @Override
    public void addRateReview(Long id, Long userId, boolean useful) {
        update(SQL_INSERT_RATE_REVIEW, userId, id, useful);
    }

    @Override
    public boolean deleteRateReview(Long reviewId, Long userId, boolean useful) {
        int updated = update(SQL_DELETE_RATE_REVIEW, userId, reviewId, useful);
        return updated == 1;
    }



    @Override
    public void updateRateReview(Long reviewId, Long userId, boolean useful) {
        int updated = update(SQL_UPDATE_RATE_REVIEW, useful, userId, reviewId);

    }
}
