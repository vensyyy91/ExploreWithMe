package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.Initiator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(required = false) @Length(min = 1, max = 7000) String text,
                                        @RequestParam(required = false) Set<Long> categories,
                                        @RequestParam(required = false) Boolean paid,
                                        @RequestParam(required = false)
                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                        LocalDateTime rangeStart,
                                        @RequestParam(required = false)
                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                        LocalDateTime rangeEnd,
                                        @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                        @RequestParam(required = false) String sort,
                                        @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                        @RequestParam(defaultValue = "10") @Positive int size,
                                        HttpServletRequest request) {
        log.info("Получен запрос GET /events?from={}&size={}\n" +
                        "Параметры:\ntext={}\ncategories={}\npaid={}\nrangeStart={}\nrangeEnd={}\nonlyAvailable={}\nsort={}",
                from, size, text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort);
        return eventService.getPublishedEvents(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request
        );
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable long id, HttpServletRequest request) {
        log.info("Получен запрос GET /events/{}", id);
        return eventService.getPublishedEventById(id, request);
    }

    @GetMapping("/initiators")
    public List<Initiator> getTopEventInitiators(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                 @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Получен запрос GET /events/initiators?from={}&size={}", from, size);
        return eventService.getTopEventInitiators(from, size);
    }
}