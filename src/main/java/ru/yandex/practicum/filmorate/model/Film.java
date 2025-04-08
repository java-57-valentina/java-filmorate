package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.exception.AlreadyLikedException;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;


@Data
public class Film {
    public static final int MAX_DESCRIPTION_SIZE = 200;

    @NotNull (groups = AdvanceInfo.class)
    private Long id;

    @NotBlank (groups = BasicInfo.class)
    private String name;

    @Size(max = MAX_DESCRIPTION_SIZE, message = "Максимальная длина описания - 200 символов",
            groups = {BasicInfo.class, AdvanceInfo.class})
    private String description;

    @NotNull (groups = BasicInfo.class)
    private LocalDate releaseDate;

    @NotNull (groups = BasicInfo.class)
    @Min(value = 1, message = "Продолжительность должна быть положительным числом",
            groups = {BasicInfo.class, AdvanceInfo.class})
    private Integer duration;

    private Set<Long> likes = new LinkedHashSet<>();

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
