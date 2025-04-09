package ru.yandex.practicum.filmorate.exception;

public class EmailAlreadyTakenException extends RuntimeException {
    public EmailAlreadyTakenException(String email) {
        super("User with email " + email + " is already registered");
    }
}
