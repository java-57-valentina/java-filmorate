package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.rating.RatingRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {

    final RatingRowMapper ratingRowMapper;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("title"));
        film.setDescription(rs.getString("description"));
        film.setDuration(rs.getInt("duration"));
//        Rating mpa = new Mpa(
//                rs.getInt("rating_id"),
//                rs.getString("mpa"));
        Rating rating = ratingRowMapper.mapRow(rs, rowNum);
        film.setMpa(rating);
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        return film;
    }
}
