package ru.practicum.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.category.CategoryDto;
import ru.practicum.enums.State;
import ru.practicum.user.UserShortDto;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class EventFullDto {
    private Long id;
    @NotNull
    private String annotation;
    @NotNull
    private CategoryDto category;
    private Integer confirmedRequests;
    private String createdOn;
    private String description;
    @NotNull
    private String eventDate;
    @NotNull
    private UserShortDto initiator;
    @NotNull
    private Location location;
    @NotNull
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private State state;
    @NotNull
    private String title;
    private Long views;
}