package ru.yandex.practicum.filmorate.storage.like;

public interface LikeStorage {
    void save(Long id, Long userId);

    void delete(Long filmId, Long userId);

    boolean existsLike(Long filmId, Long userId);

}
