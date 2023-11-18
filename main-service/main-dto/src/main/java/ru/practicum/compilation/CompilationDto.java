package ru.practicum.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.event.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class CompilationDto {
    private long id;
    List<EventShortDto> events;
    @NotNull
    private boolean pinned;
    @NotBlank
    private String title;
}