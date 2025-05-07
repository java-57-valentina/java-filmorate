package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.AlreadyFriendException;
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
    private static final String SQL_CHECK_FRIENDSHIP = """
            SELECT EXISTS (
                SELECT 1 FROM friendship
                WHERE user_id = ? AND friend_id = ?
            )
            """;
    private static final String SQL_ADD_FRIEND = """
            INSERT INTO friendship(user_id, friend_id)
            VALUES(?, ?)
            """;
    private static final String SQL_SELECT_FRIENDS = """
            SELECT u.*
            FROM users u
            JOIN friendship f ON u.id = f.friend_id
            WHERE f.user_id = ?;
            """;
    private static final String SQL_SELECT_COMMON_FRIENDS = """
            SELECT u.*
            FROM friendship f1
            JOIN friendship f2 ON f1.friend_id = f2.friend_id
            JOIN users u ON u.id = f1.friend_id
            WHERE f1.user_id = ?
              AND f2.user_id = ?
            """;
    private static final String SQL_REMOVE_FRIEND = """
            DELETE FROM friendship
            WHERE user_id = ? AND friend_id = ?
            """;
    private static final String SQL_CHECK_USER_EXISTS = "SELECT EXISTS(SELECT 1 FROM users WHERE id = ?)";


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
    public void checkUserExists(Long id) throws NotFoundException {
        Boolean found = jdbcTemplate.queryForObject(
                SQL_CHECK_USER_EXISTS, Boolean.class, id);

        if (found == null || !found)
            throw new NotFoundException("User id:" + id + " not found");
    }

    @Override
    public User getUser(Long id) {
        Optional<User> one = getOne(SQL_SELECT_ONE, id);
        if (one.isEmpty())
            throw new NotFoundException("User id:" + id + " not found");
        return one.get();
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        checkUserExists(id);
        checkUserExists(friendId);

        Boolean alreadyAdded = jdbcTemplate.queryForObject(
                SQL_CHECK_FRIENDSHIP,
                Boolean.class,
                id,
                friendId
        );

        if (Boolean.TRUE.equals(alreadyAdded)) {
            throw new AlreadyFriendException(id, friendId);
        }

        // Создаём/принимаем заявку
        int updatedRows = update(SQL_ADD_FRIEND, id, friendId);
    }

    @Override
    public void removeFriend(Long id, Long friendId) {
        checkUserExists(id);
        checkUserExists(friendId);
        int updatedRows = update(SQL_REMOVE_FRIEND, id, friendId);
    }

    @Override
    public Collection<User> getFriendsOfUser(Long id) {
        checkUserExists(id);
        return getMany(SQL_SELECT_FRIENDS, id);
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long otherId) {
        checkUserExists(id);
        checkUserExists(otherId);
        return getMany(SQL_SELECT_COMMON_FRIENDS, id, otherId);
    }
}
