package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
    public ResponseEntity<Object> hit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        log.info("Получен запрос POST /hit");
        return statsClient.addHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam @NotBlank String start,
                                           @RequestParam @NotBlank String end,
                                           @RequestParam(required = false) Set<String> uris,
                                           @RequestParam(required = false) boolean unique) {
        log.info("Получен запрос GET /stats?start={}&end={}&uris={}&unique={}", start, end, uris, unique);
        return statsClient.getStats(start, end, uris, unique);
    }
}