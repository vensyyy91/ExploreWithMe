package ru.practicum;

import ru.practicum.category.CategoryDto;
import ru.practicum.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryById(long catId);

    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(long catId);

    CategoryDto updateCategory(long catId, CategoryDto categoryDto);
}