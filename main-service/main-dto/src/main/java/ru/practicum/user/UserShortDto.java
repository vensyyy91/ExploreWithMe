package ru.practicum.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserShortDto {
    private long id;
    @NotBlank
    private String name;
}