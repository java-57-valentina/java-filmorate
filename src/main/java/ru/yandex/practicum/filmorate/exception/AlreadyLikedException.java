package ru.yandex.practicum.filmorate.exception;

public class AlreadyLikedException extends RuntimeException {
    public AlreadyLikedException(Long id, Long userId) {
        super("Film id:" + id + " already was liked by user id:" + userId);
    }
}
