package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatsClientController {
    private final StatsClient statsClient;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EndpointHitDto> hit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        log.info("Получен запрос POST /hit");
        return statsClient.addHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStats>> getStats(@RequestParam
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                    LocalDateTime start,
                                                    @RequestParam
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                    LocalDateTime end,
                                                    @RequestParam(required = false) Set<String> uris,
                                                    @RequestParam(required = false) boolean unique) {
        log.info("Получен запрос GET /stats?start={}&end={}&uris={}&unique={}", start, end, uris, unique);
        return statsClient.getStats(start, end, uris, unique);
    }
}