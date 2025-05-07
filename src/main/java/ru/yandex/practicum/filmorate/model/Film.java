package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.exception.AlreadyLikedException;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


@Data
public class Film {
    public static final int MAX_DESCRIPTION_SIZE = 200;

    private Long id;

    private String name;

    private String description;

    private LocalDate releaseDate;

    private Integer duration;

    private Set<Long> likes = new LinkedHashSet<>();

    private Rating mpa;

    private Collection<Genre> genres = List.of();

    public void addLike(Long userId) {
        if (!likes.add(userId))
            throw new AlreadyLikedException(id, userId);
    }

    public void removeLike(Long userId) {
        if (!likes.remove(userId))
            throw new LikeNotFoundException(id, userId);
    }

    public void clearLikes() {
        likes.clear();
    }
}
