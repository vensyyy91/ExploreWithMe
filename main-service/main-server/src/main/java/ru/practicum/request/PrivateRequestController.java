package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestController {
    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getUserRequests(@PathVariable long userId) {
        log.info("Получен запрос GET /users/{}/requests", userId);
        return requestService.getUserRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addNewRequest(@PathVariable long userId,
                                                 @RequestParam long eventId) {
        log.info("Получен запрос POST /users/{}/requests?eventId={}", userId, eventId);
        return requestService.addNewRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable long userId,
                                                 @PathVariable long requestId) {
        log.info("Получен запрос PATCH /users/{}/requests/{}/cancel", userId, requestId);
        return requestService.cancelRequest(userId, requestId);
    }
}