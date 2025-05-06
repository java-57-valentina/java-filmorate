package ru.yandex.practicum.filmorate.exception;

public class FriendNotFoundException extends RuntimeException {
    public FriendNotFoundException(Long id, Long friendId) {
        super("User id:" + friendId + " is not a friend for user id:" + id);
    }
}
