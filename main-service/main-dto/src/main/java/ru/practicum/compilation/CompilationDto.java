package ru.practicum.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.event.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class CompilationDto {
    private Long id;
    Set<EventShortDto> events;
    @NotNull
    private Boolean pinned;
    @NotBlank
    private String title;
}