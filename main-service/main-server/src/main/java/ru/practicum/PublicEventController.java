package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.EventFullDto;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam String text,
                                                 @RequestParam Set<Long> categories,
                                                 @RequestParam boolean paid,
                                                 @RequestParam String rangeStart,
                                                 @RequestParam String rangeEnd,
                                                 @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                                 @RequestParam String sort,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос GET /events");
        return eventService.getPublishedEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable long id) {
        log.info("Получен запрос GET /events/{}", id);
        return eventService.getPublishedEventById(id);
    }
}