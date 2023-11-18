package ru.practicum.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.enums.StateActionAdmin;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateEventAdminRequest {
    @Length(min = 20, max = 2000, message = "Event annotation length must be between 20 and 2000 characters")
    private String annotation;
    private long category;
    @Length(min = 20, max = 7000, message = "Event description length must be between 20 and 7000 characters")
    private String description;
    private String eventDate;
    private Location location;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private StateActionAdmin stateActionAdmin;
    @Length(min = 3, max = 120, message = "Event title length must be between 3 and 120 characters")
    private String title;
}