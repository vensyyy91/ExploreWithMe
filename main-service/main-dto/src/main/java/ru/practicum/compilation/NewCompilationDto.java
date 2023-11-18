package ru.practicum.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class NewCompilationDto {
    List<Integer> events;
    private boolean pinned;
    @NotNull
    @Length(min = 1, max = 50, message = "Compilation title length must be between 1 and 50 characters")
    private String title;
}