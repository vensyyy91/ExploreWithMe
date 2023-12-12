package ru.practicum.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @NotNull
    @Email
    @Size(min = 6, max = 254, message = "User email length must be between 6 and 254 characters")
    private String email;
    @NotBlank
    @Size(min = 2, max = 250, message = "User name length must be between 2 and 250 characters")
    private String name;
}