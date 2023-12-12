package ru.practicum.mark;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/marks")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicMarkController {
    private final MarkService markService;

    @GetMapping
    public List<MarkDto> getRecentMarks(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                        @RequestParam(defaultValue = "10") @Positive int size) {
        return markService.getRecentMarks(from, size);
    }

    @GetMapping("/{eventId}")
    public List<MarkDto> getAllEventMarks(@PathVariable long eventId,
                                          @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                          @RequestParam(defaultValue = "10") @Positive int size) {
        return markService.getAllEventMarks(eventId, from, size);
    }
}