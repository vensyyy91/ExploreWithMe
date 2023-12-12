package ru.practicum.mark;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/marks")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PrivateMarkController {
    private final MarkService markService;

    @GetMapping
    public List<MarkDto> getUserMarks(@PathVariable long userId,
                                      @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                      @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Получен запрос GET /users/{}/marks", userId);
        return markService.getUserMarks(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MarkDto addMark(@PathVariable long userId,
                           @RequestParam long eventId,
                           @RequestBody @Valid NewMarkDto newMarkDto) {
        log.info("Получен запрос POST /users/{}/marks?eventId={}\nТело запроса: {}", userId, eventId, newMarkDto);
        return markService.addMark(userId, eventId, newMarkDto);
    }

    @PatchMapping("/{markId}")
    public MarkDto updateMark(@PathVariable long userId,
                              @PathVariable long markId,
                              @RequestBody @Valid UpdateMarkRequest updateMarkRequest) {
        log.info("Получен запрос PATCH /users/{}/marks/{}\nТело запроса: {}", userId, markId, updateMarkRequest);
        return markService.updateMark(userId, markId, updateMarkRequest);
    }

    @DeleteMapping("/{markId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMark(@PathVariable long userId, @PathVariable long markId) {
        log.info("Получен запрос DELETE /users/{}/marks/{}", userId, markId);
        markService.deleteMark(userId, markId);
    }
}