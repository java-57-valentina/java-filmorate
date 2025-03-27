package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {

    @NotNull (groups = AdvanceInfo.class)
    private Long id;

    @NotNull (groups = BasicInfo.class)
    @Email (groups = BasicInfo.class)
    private String email;

    @NotNull (groups = BasicInfo.class)
    @NotBlank (groups = BasicInfo.class)
    @Pattern(regexp = "[^ ]*")
    private String login;

    private String name;

    @NotNull (groups = BasicInfo.class)
    @Past (groups = BasicInfo.class)
    private LocalDate birthday;
}

