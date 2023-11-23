package ru.practicum.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.category.CategoryDto;
import ru.practicum.user.UserShortDto;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class EventShortDto {
    private Long id;
    @NotNull
    private String annotation;
    @NotNull
    private CategoryDto category;
    private Integer confirmedRequests;
    @NotNull
    private String eventDate;
    @NotNull
    private UserShortDto initiator;
    @NotNull
    private Boolean paid;
    @NotNull
    private String title;
    private Long views;
}