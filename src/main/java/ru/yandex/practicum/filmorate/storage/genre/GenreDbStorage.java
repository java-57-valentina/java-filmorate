package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class GenreDbStorage extends BaseStorage<Genre> implements GenreStorage {

    private static final String SQL_SELECT_ALL =  "SELECT * FROM genres ORDER BY id";
    private static final String SQL_INSERT = "INSERT INTO genres (name) VALUES (?)";
    private static final String SQL_SELECT_ONE = "SELECT * FROM genres WHERE id = ?";
    private static final String SQL_UPDATE = "UPDATE genres SET name = ? WHERE id = ?";
    private static final String SQL_DELETE_ONE = "DELETE FROM genres WHERE id = ?";

    public GenreDbStorage(JdbcTemplate jdbcTemplate, GenreRowMapper rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    @Override
    public Collection<Genre> findAll() {
        return getMany(SQL_SELECT_ALL);
    }

    @Override
    public Genre create(Genre genre) {
        Short id = insertAndReturnId(SQL_INSERT,
                Short.class,
                genre.getName());

        genre.setId(id);
        return genre;
    }

    @Override
    public Genre getGenre(Short id) {
        Optional<Genre> one = getOne(SQL_SELECT_ONE, id);
        if (one.isEmpty())
            throw new NotFoundException("Genre id:" + id + " not found");
        return one.get();
    }

    @Override
    public Genre update(Genre genre) {
        int update = update(SQL_UPDATE, genre.getName(), genre.getId());
        if (update > 0)
            return genre;
        return null;
    }

    @Override
    public boolean delete(Short id) {
        int update = update(SQL_DELETE_ONE, id);
        return update > 0;
    }

    @Override
    public List<Short> checkAllExists(Collection<Short> idsToCheck) {
        if (idsToCheck.isEmpty()) {
            return Collections.emptyList();
        }

        String valuesList = idsToCheck.stream()
                .map(n -> "(" + n + ")")
                .collect(Collectors.joining(", "));

        String sql = "SELECT DISTINCT * " +
                "FROM (VALUES " + valuesList + ") AS X(id) " +
                "WHERE X.id NOT IN (SELECT id FROM genres)" ;
        List<Short> invalidIds = jdbcTemplate.queryForList(sql, Short.class);
        System.out.println("invalidIds = " + invalidIds);
        return invalidIds;
    }
}
