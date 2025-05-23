package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.AlreadyFriendException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;

import java.util.*;

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
    private static final String SQL_GET_USERS_LIKES = """
            SELECT l.*
            FROM likes l
            """;
    private static final String SQL_CHECK_USER_EXISTS = "SELECT EXISTS(SELECT 1 FROM users WHERE id = ?)";
    private static final String SQL_CHECK_EMAIL_USED = "SELECT EXISTS(SELECT 1 FROM users WHERE email = ?)";

    private final ResultSetExtractor<Map<Long, Set<Long>>> usersLikesExtractor;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, UserRowMapper rowMapper,
                         ResultSetExtractor<Map<Long, Set<Long>>> usersLikesExtractor) {
        super(jdbcTemplate, rowMapper);
        this.usersLikesExtractor = usersLikesExtractor;
    }

    @Override
    public Collection<User> getAll() {
        return getMany(SQL_SELECT_ALL);
    }

    @Override
    public User save(User user) {
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
        boolean exists = exists(SQL_CHECK_USER_EXISTS, id);
        if (!exists)
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
    public boolean isEmailUsed(String email) {
        return exists(SQL_CHECK_EMAIL_USED, email);
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

    @Override
    public Set<Long> getRecommendationsIds(Long id) {
        checkUserExists(id);
        Map<Long, Set<Long>> usersLikes = jdbcTemplate.query(SQL_GET_USERS_LIKES, usersLikesExtractor);

        // если у пользователя нет лайков, нет и рекомендаций
        if (!usersLikes.containsKey(id)) {
            return Set.of();
        }

        Set<Long> currentUserLikes = usersLikes.get(id);
        Set<Long> recommendationsIds = new HashSet<>();
        long currentCommonLikesCount;
        long maxCommonLikesCount = 0;

        for (long userId : usersLikes.keySet()) {
            if (userId == id) {
                continue;
            }
            Set<Long> otherUserLikes = new HashSet<>(usersLikes.get(userId));
            otherUserLikes.retainAll(currentUserLikes);
            currentCommonLikesCount = otherUserLikes.size();
            if (currentCommonLikesCount < maxCommonLikesCount) {
                continue;
            }
            if (currentCommonLikesCount > maxCommonLikesCount) {
                maxCommonLikesCount = currentCommonLikesCount;
                recommendationsIds.clear();
            }
            recommendationsIds.addAll(usersLikes.get(userId));
        }
        // нет ни с кем ни одного общего лайка, то нет рекомендаций
        if (maxCommonLikesCount == 0) {
            return Set.of();
        }
        recommendationsIds.removeAll(usersLikes.get(id));
        return recommendationsIds;
    }
}
