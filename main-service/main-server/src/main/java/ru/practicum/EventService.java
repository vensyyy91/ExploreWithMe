package ru.practicum;

import ru.practicum.event.*;

import java.util.List;
import java.util.Set;

public interface EventService {
    List<EventFullDto> getAllEvents(Set<Integer> users,
                                    Set<String> states,
                                    Set<Integer> categories,
                                    String rangeStart,
                                    String rangeEnd,
                                    int from,
                                    int size);

    EventFullDto updateEvent(long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventFullDto> getPublishedEvents(String text,
                                          Set<Long> categories,
                                          boolean paid,
                                          String rangeStart,
                                          String rangeEnd,
                                          boolean onlyAvailable,
                                          String sort,
                                          int from,
                                          int size);

    EventFullDto getPublishedEventById(long id);

    List<EventShortDto> getUserEvents(long userId, int from, int size);

    EventFullDto addNewEvent(long userId, NewEventDto newEventDto);

    EventFullDto getUserEventById(long userId, long eventId);

    EventFullDto updateUserEvent(long userId, long eventId, UpdateEventUserRequest updateRequest);

    List<ParticipationRequestDto> getUserEventRequests(long userId, long eventId);

    EventRequestStatusUpdateResult updateUserEventRequestsStatus(long userId,
                                                                 long eventId,
                                                                 EventRequestStatusUpdateRequest request);
}