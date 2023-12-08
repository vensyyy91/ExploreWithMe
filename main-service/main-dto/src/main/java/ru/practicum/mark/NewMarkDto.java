package ru.practicum.mark;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewMarkDto {
    @NotNull
    @Min(0)
    @Max(10)
    private int mark;
    @Length(min = 3, max = 1000, message = "Message length must be between 3 and 1000 characters")
    private String message;
}