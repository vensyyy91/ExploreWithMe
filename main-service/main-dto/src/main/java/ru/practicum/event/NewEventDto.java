package ru.practicum.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class NewEventDto {
    @NotNull
    @Length(min = 20, max = 2000, message = "Event annotation length must be between 20 and 2000 characters")
    private String annotation;
    @NotNull
    private long category;
    @NotNull
    @Length(min = 20, max = 7000, message = "Event description length must be between 20 and 7000 characters")
    private String description;
    @NotNull
    private String eventDate;
    @NotNull
    private Location location;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    @NotNull
    @Length(min = 3, max = 120, message = "Event title length must be between 3 and 120 characters")
    private String title;
}