package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.AlreadyFriendException;
import ru.yandex.practicum.filmorate.exception.FriendNotFoundException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
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

    public void addFriend(Long id) {
        if (!friends.add(id))
            throw new AlreadyFriendException(this.id, id);
    }

    public void removeFriend(Long id) {
        if (!friends.remove(id))
            throw new FriendNotFoundException(this.id, id);
    }
}

