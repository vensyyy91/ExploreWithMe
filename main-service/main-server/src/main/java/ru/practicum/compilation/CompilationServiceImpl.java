package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.NotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CompilationDto> getCompilations(boolean pinned, int from, int size) {
        List<Compilation> compilations = compilationRepository.findAllByPinned(
                pinned,
                PageRequest.of(from / size, size)
        ).getContent();
        log.info("Возвращен список подборок: {}", compilations);

        return compilations.stream().map(CompilationMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(long compId) {
        Compilation compilation = getCompilation(compId);
        log.info("Возвращена подборка: {}", compilation);

        return CompilationMapper.toDto(compilation);
    }

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Set<Event> events = new HashSet<>(eventRepository.findAllById(newCompilationDto.getEvents()));
        Compilation compilation = CompilationMapper.fromDto(newCompilationDto, events);
        Compilation newCompilation = compilationRepository.save(compilation);
        log.info("Добавлена новая подборка: {}", newCompilation);

        return CompilationMapper.toDto(compilation);
    }

    @Override
    @Transactional
    public void deleteCompilation(long compId) {
        getCompilation(compId);
        compilationRepository.deleteById(compId);
        log.info("Удалена подборка с id={}", compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = getCompilation(compId);
        Set<Long> eventsForUpdate = updateCompilationRequest.getEvents();
        Boolean pinnedForUpdate = updateCompilationRequest.getPinned();
        String titleForUpdate = updateCompilationRequest.getTitle();
        if (eventsForUpdate != null) {
            compilation.setEvents(new HashSet<>(eventRepository.findAllById(eventsForUpdate))); //TODO: event not found???
        }
        if (pinnedForUpdate != null) {
            compilation.setPinned(pinnedForUpdate);
        }
        if (titleForUpdate != null) {
            compilation.setTitle(titleForUpdate);
        }
        log.info("Обновлена подборка: {}", compilation);

        return CompilationMapper.toDto(compilation);
    }

    private Compilation getCompilation(long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
    }
}