package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString
@EqualsAndHashCode
public class User {

    @NotNull (groups = AdvanceInfo.class)
    private Long id;

    @NotBlank (groups = BasicInfo.class)
    @Email (groups = {BasicInfo.class, AdvanceInfo.class})
    private String email;

    @NotBlank (groups = BasicInfo.class)
    @Pattern(regexp = "[^ ]*")
    private String login;

    private String name;

    @NotNull (groups = BasicInfo.class)
    @Past (groups = {BasicInfo.class, AdvanceInfo.class})
    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();

    public boolean addFriend(Long id) {
        return friends.add(id);
    }

    public boolean removeFriend(Long id) {
        return friends.remove(id);
    }
}

