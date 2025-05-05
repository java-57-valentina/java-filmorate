package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class GenreDbStorage extends BaseStorage<Genre> {

    private static final String SQL_SELECT_ALL =  "SELECT * FROM genres";
    private static final String SQL_INSERT = "INSERT INTO genres (name) VALUES (?)";
    private static final String SQL_SELECT_ONE = "SELECT * FROM genres WHERE id = ?";
    private static final String SQL_UPDATE = "UPDATE genres SET name = ? WHERE id = ?";
    private static final String SQL_DELETE_ONE = "DELETE FROM genres WHERE id = ?";

    public GenreDbStorage(JdbcTemplate jdbcTemplate, GenreRowMapper rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    public Collection<Genre> findAll() {
        return getMany(SQL_SELECT_ALL);
    }

    public Genre create(Genre genre) {
        Integer id = insertAndReturnId(SQL_INSERT,
                Integer.class,
                genre.getName());

        genre.setId(id);
        return genre;
    }

    public Genre getGenre(Integer id) {
        Optional<Genre> one = getOne(SQL_SELECT_ONE, id);
        if (one.isEmpty())
            throw new NotFoundException("Genre id:" + id + " not found");
        return one.get();
    }

    public Genre update(Genre genre) {
        int update = update(SQL_UPDATE, genre.getName(), genre.getId());
        if (update > 0)
            return genre;
        return null;
    }

    public boolean delete(Integer id) {
        int update = update(SQL_DELETE_ONE, id);
        return update > 0;
    }
}
