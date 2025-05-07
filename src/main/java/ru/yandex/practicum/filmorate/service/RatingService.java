package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.RatingDto;
import ru.yandex.practicum.filmorate.mapper.RatingMapper;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.rating.RatingDbStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingDbStorage ratingStorage;
    private final RatingMapper ratingMapper;

    public Collection<RatingDto> getAll() {
        return ratingStorage.getAll()
                .stream()
                .map(ratingMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public RatingDto getRating(Short id) {
        Rating found = ratingStorage.getRating(id);
        return ratingMapper.mapToDto(found);
    }
}
