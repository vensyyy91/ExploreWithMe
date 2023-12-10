package ru.practicum.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.validation.ValidEventDate;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000, message = "Event annotation length must be between 20 and 2000 characters")
    private String annotation;
    @NotNull
    private Long category;
    @NotBlank
    @Size(min = 20, max = 7000, message = "Event description length must be between 20 and 7000 characters")
    private String description;
    @NotNull
    @ValidEventDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    @Valid
    private Location location;
    private boolean paid;
    @PositiveOrZero
    private int participantLimit;
    @Builder.Default
    private boolean requestModeration = true;
    @NotBlank
    @Size(min = 3, max = 120, message = "Event title length must be between 3 and 120 characters")
    private String title;
}