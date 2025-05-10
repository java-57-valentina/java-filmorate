package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.Mpa;

@UtilityClass
public class MpaMapper {
    public MpaDto mapToDto(Mpa mpa) {
        MpaDto mpaDto = new MpaDto();
        mpaDto.setId(mpa.getId());
        mpaDto.setName(mpa.getName());
        return mpaDto;
    }

    public Mpa mapFromDto(MpaDto mpaDto) {
        return new Mpa(
                mpaDto.getId(),
                mpaDto.getName());
    }
}
