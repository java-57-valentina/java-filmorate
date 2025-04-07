package ru.yandex.practicum.filmorate.exception;

public class FriendNotFoundException extends RuntimeException {
    public FriendNotFoundException(Long id1, Long id2) {
        super("User id:" + id1 + " doesn't have a friend id:" + id2);
    }
}
