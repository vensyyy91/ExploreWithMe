package ru.practicum.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class NewCategoryDto {
    @NotNull
    @Length(min = 1, max = 50, message = "Category name length must be between 1 and 50 characters")
    private String name;
}