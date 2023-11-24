package ru.practicum.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    Set<Long> events;
    private Boolean pinned;
    @NotBlank
    @Length(min = 1, max = 50, message = "Compilation title length must be between 1 and 50 characters")
    private String title;
}