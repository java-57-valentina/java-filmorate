package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

public interface ReviewStorage {

    Collection<Review> getAll();

    Collection<Review> getByFilm(Long filmId, int count);

    Review getReview(Long id);

    Review save(Review review);

    boolean delete(Long id);

    Review update(Review review);

    boolean isReviewExists(Long userId, Long filmId);

    boolean isReviewRatedByUser(Long userId, Long reviewId);

    void addRateReview(Long id, Long userId, boolean useful);

    boolean updateRateReview(Long reviewId, Long userId, boolean useful);

    void deleteRateReview(Long reviewId, Long userId);
}
