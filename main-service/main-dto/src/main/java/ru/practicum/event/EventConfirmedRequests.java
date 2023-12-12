package ru.practicum.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventConfirmedRequests {
    private long eventId;
    private long count;
}