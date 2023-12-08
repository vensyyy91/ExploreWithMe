package ru.practicum.event;

import ru.practicum.request.EventRequestStatusUpdateRequest;
import ru.practicum.request.EventRequestStatusUpdateResult;
import ru.practicum.request.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventService {
    List<EventFullDto> getAllEvents(Set<Long> users,
                                    Set<String> states,
                                    Set<Long> categories,
                                    LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd,
                                    int from,
                                    int size);

    EventFullDto updateEventByAdmin(long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventShortDto> getPublishedEvents(String text,
                                          Set<Long> categories,
                                          Boolean paid,
                                          LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd,
                                          boolean onlyAvailable,
                                          String sort,
                                          int from,
                                          int size,
                                          HttpServletRequest request);

    EventFullDto getPublishedEventById(long id, HttpServletRequest request);

    List<EventShortDto> getUserEvents(long userId, int from, int size);

    EventFullDto addNewEvent(long userId, NewEventDto newEventDto);

    EventFullDto getUserEventById(long userId, long eventId);

    EventFullDto updateUserEvent(long userId, long eventId, UpdateEventUserRequest updateRequest);

    List<ParticipationRequestDto> getUserEventRequests(long userId, long eventId);

    EventRequestStatusUpdateResult updateUserEventRequestsStatus(long userId,
                                                                 long eventId,
                                                                 EventRequestStatusUpdateRequest request);
}