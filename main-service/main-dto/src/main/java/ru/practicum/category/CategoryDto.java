package ru.practicum.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDto {
    private long id;
    @NotNull
    @Length(min = 1, max = 50, message = "Category name length must be between 1 and 50 characters")
    private String name;
}