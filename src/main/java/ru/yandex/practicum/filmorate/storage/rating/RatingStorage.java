package ru.yandex.practicum.filmorate.storage.rating;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;

public interface RatingStorage {

    Collection<Rating> findAll();

    Rating getRating(Short id);
}
