package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class UsersLikesExtractor implements ResultSetExtractor<Map<Long, Set<Long>>> {
    @Override
    public Map<Long, Set<Long>> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Set<Long>> usersLikes = new HashMap<>();

        while (rs.next()) {
            long userId = rs.getLong("user_Id");
            long filmId = rs.getLong("film_Id");
            if (!usersLikes.containsKey(userId)) {
                usersLikes.put(userId, new HashSet<>());
            }
            usersLikes.get(userId).add(filmId);
        }
        return usersLikes;
    }
}
