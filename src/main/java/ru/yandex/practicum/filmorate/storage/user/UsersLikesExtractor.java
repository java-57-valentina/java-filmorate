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
            long user_id = rs.getLong("user_id");
            long film_id = rs.getLong("film_id");
            if (!usersLikes.containsKey(user_id)) {
                usersLikes.put(user_id, new HashSet<>());
            }
            usersLikes.get(user_id).add(film_id);
        }
        return usersLikes;
    }
}
