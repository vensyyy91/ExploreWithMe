package ru.practicum;

import ru.practicum.event.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getUserRequests(long userId);

    ParticipationRequestDto addNewRequest(long userId, long eventId);

    ParticipationRequestDto cancelRequest(long userId, long requestId);
}