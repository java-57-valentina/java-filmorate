package ru.yandex.practicum.filmorate.storage.director;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;

import java.util.Collection;
import java.util.Optional;


@Repository("directorDbStorage")
public class DirectorDbStorage extends BaseStorage<Director> implements DirectorStorage {
    public DirectorDbStorage(JdbcTemplate jdbcTemplate, DirectorRowMapper rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    private static final String SQL_SELECT_ALL = "SELECT * FROM directors;";
    private static final String SQL_SELECT_ONE = "SELECT * FROM directors WHERE id=?;";
    private static final String SQL_DELETE_ONE = "DELETE FROM directors WHERE id=?;";
    private static final String SQL_INSERT = "INSERT INTO directors (name) VALUES (?);";
    private static final String SQL_UPDATE_DIRECTOR = "UPDATE directors SET name =? WHERE id=?;";

    @Override
    public Collection<Director> getAll() {
        Collection<Director> many = getMany(SQL_SELECT_ALL);
        return many;
    }

    @Override
    public Director getDirector(Integer id) {
        Optional<Director> one = getOne(SQL_SELECT_ONE, id);
        return one.orElseThrow(() -> new NotFoundException("Director id:" + id + " not found"));

    }

    @Override
    public void removeDirector(Integer id) {
        delete(SQL_DELETE_ONE, id);
    }

    @Override
    public Director create(Director director) {
        Integer id = insertAndReturnId(SQL_INSERT, Integer.class, director.getName());
        if (id == null) {
            return null;
        }
        director.setId(id);
        return director;
    }

    @Override
    public Director update(Director director) {
        super.update(SQL_UPDATE_DIRECTOR, director.getName(), director.getId());
        return getDirector(director.getId());
    }
}
