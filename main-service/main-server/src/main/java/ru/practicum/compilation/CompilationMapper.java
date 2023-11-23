package ru.practicum.compilation;

import lombok.experimental.UtilityClass;
import ru.practicum.event.Event;
import ru.practicum.event.EventMapper;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public CompilationDto toDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(compilation.getEvents().stream().map(EventMapper::toShortDto).collect(Collectors.toSet()))
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    /*public Compilation fromDto(CompilationDto compilationDto) {
        return Compilation.builder()
                .id(compilationDto.getId())
                .events(compilationDto.getEvents().stream().map(EventMapper::fromDto).collect(Collectors.toSet()))
                .pinned(compilationDto.getPinned())
                .title(compilationDto.getTitle())
                .build();
    }*/

    public Compilation fromDto(NewCompilationDto newCompilationDto, Set<Event> events) {
        return Compilation.builder()
                .events(events)
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }

    /*public Compilation fromDto(UpdateCompilationRequest request, Set<Event> events) {
        return Compilation.builder()
                .events(events)
                .pinned(request.getPinned())
                .title(request.getTitle())
                .build();
    }*/
}