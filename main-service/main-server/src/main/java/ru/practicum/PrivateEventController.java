package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getUserEvents(@PathVariable long userId,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос GET /users/{}/events", userId);
        return eventService.getUserEvents(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable long userId,
                                 @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Получен запрос POST /users/{}/events", userId);
        return eventService.addNewEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEventById(@PathVariable long userId,
                                         @PathVariable long eventId) {
        log.info("Получен запрос GET /users/{}/events/{}", userId, eventId);
        return eventService.getUserEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateUserEvent(@PathVariable long userId,
                                        @PathVariable long eventId,
                                        @RequestBody @Valid UpdateEventUserRequest updateRequest) {
        log.info("Получен запрос PATCH /users/{}/events/{}", userId, eventId);
        return eventService.updateUserEvent(userId, eventId, updateRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getUserEventRequests(@PathVariable long userId,
                                                              @PathVariable long eventId) {
        log.info("Получен запрос GET /users/{}/events/{}/requests", userId, eventId);
        return eventService.getUserEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateUserEventRequestsStatus(@PathVariable long userId,
                                                                        @PathVariable long eventId,
                                                                        @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("Получен запрос PATCH /users/{}/events/{}/requests", userId, eventId);
        return eventService.updateUserEventRequestsStatus(userId, eventId, request);
    }
}