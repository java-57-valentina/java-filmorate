package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReviewResponseDto {
    @JsonProperty("reviewId")
    private Long id;
    private Long userId;
    private Long filmId;
    private String content;
    private Boolean isPositive;
    private int useful;
}
