package ru.practicum.request;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getUserRequests(long userId);

    ParticipationRequestDto addNewRequest(long userId, long eventId);

    ParticipationRequestDto cancelRequest(long userId, long requestId);
}