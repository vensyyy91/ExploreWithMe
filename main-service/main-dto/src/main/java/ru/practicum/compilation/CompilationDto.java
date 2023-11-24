package ru.practicum.compilation;

import lombok.Builder;
import lombok.Data;
import ru.practicum.event.EventShortDto;

import java.util.Set;

@Data
@Builder
public class CompilationDto {
    private Long id;
    Set<EventShortDto> events;
    private Boolean pinned;
    private String title;
}