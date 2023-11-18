package ru.practicum.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.category.CategoryDto;
import ru.practicum.enums.State;
import ru.practicum.user.UserShortDto;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class EventFullDto {
    private long id;
    @NotNull
    private String annotation;
    @NotNull
    private CategoryDto category;
    private int confirmedRequests;
    private String createdOn;
    private String description;
    @NotNull
    private String eventDate;
    @NotNull
    private UserShortDto initiator;
    @NotNull
    private Location location;
    @NotNull
    private boolean paid;
    private int participantLimit;
    private String publishedOn;
    private boolean requestModeration;
    private State state;
    @NotNull
    private String title;
    private long views;
}