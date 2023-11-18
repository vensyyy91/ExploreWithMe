package ru.practicum.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.category.CategoryDto;
import ru.practicum.user.UserShortDto;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class EventShortDto {
    private long id;
    @NotNull
    private String annotation;
    @NotNull
    private CategoryDto category;
    private int confirmedRequests;
    @NotNull
    private String eventDate;
    @NotNull
    private UserShortDto initiator;
    @NotNull
    private boolean paid;
    @NotNull
    private String title;
    private long views;
}