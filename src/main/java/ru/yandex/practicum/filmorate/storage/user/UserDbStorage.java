package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;

import java.util.Collection;
import java.util.Optional;

@Primary
@Repository("userDbStorage")
public class UserDbStorage extends BaseStorage<User> implements UserStorage {

    private static final String SQL_SELECT_ALL = "SELECT * FROM users";
    private static final String SQL_INSERT_USER = """
            INSERT INTO users (login, username, email, birthday)
            VALUES (?, ?, ?, ?)
            """;

    private static final String SQL_UPDATE_USER = """
            UPDATE users
            SET login = ?, username = ?, email= ?,  birthday = ?
            WHERE id = ?
            """;
    private static final String SQL_SELECT_ONE = "SELECT * FROM users WHERE id = ?";

    public UserDbStorage(JdbcTemplate jdbcTemplate, UserRowMapper rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    @Override
    public Collection<User> getAll() {
        return getMany(SQL_SELECT_ALL);
    }

    @Override
    public User create(User user) {
        Long id = insertAndReturnId(SQL_INSERT_USER,
                Long.class,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday()
        );

        if (id == null)
            return null;
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        int updated = super.update(SQL_UPDATE_USER,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
        if (updated > 0)
            return user;
        return null;
    }

    @Override
    public User getUser(Long id) {
        Optional<User> one = getOne(SQL_SELECT_ONE, id);
        if (one.isEmpty())
            throw new NotFoundException("User id:" + id + " not found");
        return one.get();
    }
}
