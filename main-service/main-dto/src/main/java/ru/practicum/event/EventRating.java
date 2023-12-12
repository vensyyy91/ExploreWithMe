package ru.practicum.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventRating {
    private long eventId;
    private double rating;
}