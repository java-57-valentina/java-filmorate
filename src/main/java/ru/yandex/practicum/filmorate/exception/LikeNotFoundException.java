package ru.yandex.practicum.filmorate.exception;

public class LikeNotFoundException extends RuntimeException {
    public LikeNotFoundException(Long id, Long userId) {
        super("Film id:" + id + " was not liked by user id:" + userId + " yet");
    }
}
