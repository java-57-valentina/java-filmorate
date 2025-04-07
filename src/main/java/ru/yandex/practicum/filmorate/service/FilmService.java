package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    public Film addLike(Long id, Long userId) {
        Film film = filmStorage.getFilm(id);
        userStorage.getUser(userId);
        film.addLike(userId);
        log.info("User id:{} has liked film id:{}", userId, id);
        return film;
    }

    public Film removeLike(Long id, Long userId) {
        Film film = filmStorage.getFilm(id);
        userStorage.getUser(userId);
        film.removeLike(userId);
        log.info("User id:{} has unliked film id:{}", userId, id);
        return film;
    }

    public Collection<Film> getTop(int count) {
        Comparator<? super Film> comparator = Comparator.comparingInt(f -> f.getLikes().size());

        return filmStorage.getAll().stream()
                .sorted(comparator.reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
