package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.ReviewCreateDto;
import ru.yandex.practicum.filmorate.dto.ReviewResponseDto;
import ru.yandex.practicum.filmorate.dto.ReviewUpdateDto;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public Collection<ReviewResponseDto> getReviews(
            @RequestParam(required = false) Long filmId,
            @RequestParam(defaultValue = "10") int count
    ) {
        return reviewService.getReviews(filmId, count);
    }

    @GetMapping("/{id}")
    public ReviewResponseDto getReview(@PathVariable Long id) {
        return reviewService.getReview(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponseDto create(@Valid @RequestBody ReviewCreateDto review) {
        ReviewResponseDto created = reviewService.create(review);
        log.info("Review id:{} was added: {}", created.getId(), created);
        return created;
    }

    @PutMapping
    public ReviewResponseDto update(@Valid @RequestBody ReviewUpdateDto review) {
        ReviewResponseDto updated = reviewService.update(review);
        log.info("Review id:{} was updated: {}", updated.getId(), updated);
        return updated;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        reviewService.delete(id);
        log.info("Review id:{} was deleted", id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeReview(@PathVariable Long id, @PathVariable Long userId) {
        reviewService.addRateReview(id, userId, true);
        log.info("User id:{} has liked review id:{}", userId, id);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void dislikeReview(@PathVariable Long id, @PathVariable Long userId) {
        reviewService.addRateReview(id, userId, false);
        log.info("User id:{} has disliked review id:{}", userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        reviewService.removeRateReview(id, userId, true);
        log.info("User id:{} has removed his like/dislike from review id:{}", userId, id);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void removeDislike(@PathVariable Long id, @PathVariable Long userId) {
        reviewService.removeRateReview(id, userId, false);
        log.info("User id:{} has removed his like/dislike from review id:{}", userId, id);
    }
}
