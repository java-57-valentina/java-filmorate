package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.RatingDto;
import ru.yandex.practicum.filmorate.model.Rating;

@Component
public class RatingMapper {
    public RatingDto mapToDto(Rating rating) {
        RatingDto ratingDto = new RatingDto();
        ratingDto.setId(rating.getId());
        ratingDto.setName(rating.getName());
        return ratingDto;
    }

    public Rating mapFromDto(RatingDto ratingDto) {
        return new Rating(
                ratingDto.getId(),
                ratingDto.getName());
    }
}
