package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewUpdateDto {

    @NotNull
    @JsonProperty("reviewId")
    private Long id;

    @NotBlank
    private String content;

    private Boolean isPositive;
}
