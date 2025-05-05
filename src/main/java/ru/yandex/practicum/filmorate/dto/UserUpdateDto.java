package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.BasicInfo;
import ru.yandex.practicum.filmorate.model.AdvanceInfo;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserUpdateDto {
    @NotNull
    Long id;

    @Email(message = "Неверный формат email")
    String email;

    @Pattern(regexp = "[^ ]*")
    String login;

    String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past (groups = {BasicInfo.class, AdvanceInfo.class})
    LocalDate birthday;
}
