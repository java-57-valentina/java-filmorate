package ru.yandex.practicum.filmorate.exception;

public class AlreadyFriendException extends RuntimeException {
    public AlreadyFriendException(Long id1, Long id2) {
        super("User id:" + id1 + " already has friend id:" + id2);
    }
}
