package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.exception.AlreadyFriendException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
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
        friends.remove(id);
    }

    public void clearFriends() {
        friends.clear();
    }
}

