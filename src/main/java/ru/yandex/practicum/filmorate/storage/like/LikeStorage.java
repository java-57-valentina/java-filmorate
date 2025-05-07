package ru.yandex.practicum.filmorate.storage.like;

public interface LikeStorage {
    void save(Long id, Long userId);

    boolean existsLike(Long filmId, Long userId);
}
