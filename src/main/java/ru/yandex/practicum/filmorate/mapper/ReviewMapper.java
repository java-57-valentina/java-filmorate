package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.ReviewCreateDto;
import ru.yandex.practicum.filmorate.dto.ReviewResponseDto;
import ru.yandex.practicum.filmorate.model.Review;

@UtilityClass
public class ReviewMapper {

    public Review mapToReview(ReviewCreateDto request) {
        Review review = new Review();
        review.setContent(request.getContent());
        review.setPositive(request.getIsPositive());
        review.setUserId(request.getUserId());
        review.setFilmId(request.getFilmId());
        return review;
    }

    public ReviewResponseDto mapToResponseDto(Review review) {
        ReviewResponseDto responseDto = new ReviewResponseDto();
        responseDto.setId(review.getId());
        responseDto.setUserId(review.getUserId());
        responseDto.setFilmId(review.getFilmId());
        responseDto.setContent(review.getContent());
        responseDto.setIsPositive(review.isPositive());
        responseDto.setLikes(review.getLikes());
        responseDto.setDislikes(review.getDislikes());
        responseDto.setUseful(review.getLikes() - review.getDislikes());
        return responseDto;
    }
}
