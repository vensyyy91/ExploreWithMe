package ru.practicum.compilation;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateCompilationRequest {
    private List<Integer> events;
    private boolean pinned;
    @Length(min = 1, max = 50, message = "Compilation title length must be between 1 and 50 characters")
    private String title;
}