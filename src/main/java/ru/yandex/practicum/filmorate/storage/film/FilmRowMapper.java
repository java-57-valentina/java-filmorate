package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("title"));
        film.setDescription(rs.getString("description"));
        film.setDuration(rs.getInt("duration"));

        Mpa mpa = new Mpa(
                rs.getShort("mpa_id"),
                rs.getString("mpa_name")
        );

        film.setMpa(mpa);
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());

        String genresData = rs.getString("genres_data");
        Collection<Genre> genres = parseGenres(genresData);
        film.setGenres(genres);

        return film;
    }

    private Collection<Genre> parseGenres(String genresData) {

        if (genresData == null || genresData.isEmpty()) {
            return Collections.emptyList();
        }

        String[] genreInfos = genresData.split("\\|");

        return Arrays.stream(genreInfos)
                .map(info -> info.split(":"))
                .filter(parts -> parts.length == 2)
                .map(parts -> new Genre(Short.parseShort(parts[0]), parts[1]))
                .collect(Collectors.toList());
    }
}
