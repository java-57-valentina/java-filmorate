package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.ReviewCreateDto;
import ru.yandex.practicum.filmorate.dto.ReviewResponseDto;
import ru.yandex.practicum.filmorate.dto.ReviewUpdateDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;


    public ReviewResponseDto getReview(Long id) {
        Review review = reviewStorage.getReview(id);
        return ReviewMapper.mapToResponseDto(review);
    }

    public Collection<ReviewResponseDto> getReviews(Long filmId, int count) {
        Collection<Review> reviews;
        if (filmId == null)
            reviews = reviewStorage.getAll(count);
        else
            reviews = reviewStorage.getByFilm(filmId, count);

        return reviews.stream()
                    .map(ReviewMapper::mapToResponseDto)
                    .collect(Collectors.toList());
    }

    public ReviewResponseDto create(ReviewCreateDto request) {
        userStorage.checkUserExists(request.getUserId());
        filmStorage.checkFilmExists(request.getFilmId());

        if (reviewStorage.isReviewExists(request.getUserId(), request.getFilmId())) {
            throw new ValidationException("The user has already left a review for this film");
        }

        Review review = ReviewMapper.mapToReview(request);
        Review saved = reviewStorage.save(review);
        return ReviewMapper.mapToResponseDto(saved);
    }

    public ReviewResponseDto update(ReviewUpdateDto updateDto) {
        Review origin = reviewStorage.getReview(updateDto.getId());
        if (updateDto.getContent() != null)
            origin.setContent(updateDto.getContent());

        if (updateDto.getIsPositive() != null)
            origin.setPositive(updateDto.getIsPositive());

        Review updated = reviewStorage.update(origin);
        return ReviewMapper.mapToResponseDto(updated);
    }

    public void delete(Long id) {
        boolean deleted = reviewStorage.delete(id);
        if (!deleted)
            throw new NotFoundException("Review", id);
    }

    public void addRateReview(Long reviewId, Long userId, boolean useful) {
        userStorage.checkUserExists(userId);
        if (reviewStorage.isReviewRatedByUser(userId, reviewId)) {
            reviewStorage.updateRateReview(reviewId, userId, useful);
        } else {
            reviewStorage.addRateReview(reviewId, userId, useful);
        }
    }

    public void removeRateReview(Long reviewId, Long userId, boolean like) {
        reviewStorage.deleteRateReview(reviewId, userId, like);
    }
}
