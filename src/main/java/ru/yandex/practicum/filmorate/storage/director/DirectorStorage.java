package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;

public interface DirectorStorage {

    public Collection<Director> getAll();

    public Director getDirector(Integer id);

    public void removeDirector(Integer id);

    public Director create(Director director);

    public Director update(Director director);
}
