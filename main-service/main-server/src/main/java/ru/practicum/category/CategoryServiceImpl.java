package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.IllegalOperationException;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        List<Category> categories = categoryRepository.findAll(PageRequest.of(from / size, size)).getContent();
        log.info("Возвращен список категорий: {}", categories);

        return categories.stream().map(CategoryMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(long catId) {
        Category category = getCategory(catId);
        log.info("Возвращена категория: {}", category);

        return CategoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.fromDto(newCategoryDto);
        Category newCategory = categoryRepository.save(category);
        log.info("Добавлена новая категория: {}", newCategory);

        return CategoryMapper.toDto(newCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(long catId) {
        getCategory(catId);
        if (eventRepository.existsEventByCategoryId(catId)) {
            throw new IllegalOperationException("The category is not empty");
        }
        categoryRepository.deleteById(catId);
        log.info("Удалена категория с id={}", catId);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(long catId, CategoryDto categoryDto) {
        Category category = getCategory(catId);
        category.setName(categoryDto.getName());
        log.info("Обновлена категория: {}", category);
        //TODO: проверить, корректно ли обновляется при дублировании названия

        return CategoryMapper.toDto(category);
    }

    private Category getCategory(long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
    }
}