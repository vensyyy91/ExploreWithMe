package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.EventFullDto;
import ru.practicum.event.UpdateEventAdminRequest;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam Set<Integer> users,
                                        @RequestParam Set<String> states,
                                        @RequestParam Set<Integer> categories,
                                        @RequestParam String rangeStart,
                                        @RequestParam String rangeEnd,
                                        @RequestParam int from,
                                        @RequestParam int size) {
        log.info("Получен запрос GET /admin/events");
        return eventService.getAllEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable long eventId,
                                    @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Получен запрос PATCH /admin/events/{}", eventId);
        return eventService.updateEvent(eventId, updateEventAdminRequest);
    }
}