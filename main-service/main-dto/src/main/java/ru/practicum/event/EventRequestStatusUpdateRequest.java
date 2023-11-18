package ru.practicum.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.enums.Status;

import java.util.List;

@Data
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {
    private List<Integer> requestIds;
    private Status status;
}