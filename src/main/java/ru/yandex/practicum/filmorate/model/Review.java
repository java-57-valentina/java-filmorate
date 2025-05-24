package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class Review {

    private Long id;
    private String content;
    private Long userId;
    private Long filmId;
    private boolean isPositive;
    private LocalDateTime createdAt;
    private int useful = 0;
}
