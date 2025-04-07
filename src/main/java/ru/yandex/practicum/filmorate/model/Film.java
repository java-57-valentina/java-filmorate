package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;


@Data
@ToString
@EqualsAndHashCode
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

    public boolean addLike(Long userId) {
        return likes.add(userId);
    }

    public boolean removeLike(Long userId) {
        return likes.remove(userId);
    }
}
