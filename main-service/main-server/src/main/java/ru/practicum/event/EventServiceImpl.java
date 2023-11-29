package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsClient;
import ru.practicum.ViewStats;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryRepository;
import ru.practicum.enums.State;
import ru.practicum.enums.StateActionAdmin;
import ru.practicum.enums.StateActionUser;
import ru.practicum.enums.Status;
import ru.practicum.exception.IllegalOperationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.*;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final StatsClient statsClient;
    @Value("${application.name}")
    private String appName;

    @Override
    public List<EventFullDto> getAllEvents(Set<Long> users,
                                           Set<String> states,
                                           Set<Long> categories,
                                           LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd,
                                           int from,
                                           int size) {

        Specification<Event> specification = getEventQuery(users, states, categories, rangeStart, rangeEnd);
        Map<Long, Long> confirmedRequests = requestRepository.findAllEventConfirmedRequests().stream()
                .collect(Collectors.toMap(EventConfirmedRequests::getEventId, EventConfirmedRequests::getCount));
        List<Event> events = eventRepository.findAll(specification, PageRequest.of(from / size, size))
                .getContent().stream()
                .peek(event -> event.setConfirmedRequests(confirmedRequests.getOrDefault(event.getId(), 0L)))
                .collect(Collectors.toList());
        log.info("Возвращен список событий: {}", events);

        return events.stream().map(EventMapper::toFullDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(long eventId, UpdateEventAdminRequest updateRequest) {
        Event event = getEvent(eventId);
        updateEvent(event, updateRequest);
        StateActionAdmin stateAction = updateRequest.getStateAction();
        if (stateAction != null) {
            if (stateAction == StateActionAdmin.PUBLISH_EVENT) {
                if (event.getState() != State.PENDING) {
                    throw new IllegalOperationException(
                            "Cannot publish the event because it's not in the right state: " + event.getState()
                    );
                }
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (stateAction == StateActionAdmin.REJECT_EVENT) {
                if (event.getState() == State.PUBLISHED) {
                    throw new IllegalOperationException(
                            "Cannot reject the event because it's not in the right state: " + event.getState()
                    );
                }
                event.setState(State.CANCELED);
            }
        }
        log.info("Обновлено событие: {}", event);

        return EventMapper.toFullDto(event);
    }

    @Override
    public List<EventFullDto> getPublishedEvents(String text,
                                                 Set<Long> categories,
                                                 Boolean paid,
                                                 LocalDateTime rangeStart,
                                                 LocalDateTime rangeEnd,
                                                 boolean onlyAvailable,
                                                 String sort,
                                                 int from,
                                                 int size,
                                                 HttpServletRequest request) {

        Specification<Event> specification = getPublishedEventQuery(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable
        );
        Map<Long, Long> confirmedRequests = requestRepository.findAllEventConfirmedRequests().stream()
                .collect(Collectors.toMap(EventConfirmedRequests::getEventId, EventConfirmedRequests::getCount));
        List<Event> events;
        if (sort != null) {
            Sort eventSort;
            switch (sort) {
                case "EVENT_DATE":
                    eventSort = Sort.by(Sort.Direction.ASC, "eventDate");
                    break;
                case "VIEWS":
                    eventSort = Sort.by(Sort.Direction.DESC, "views");
                    break;
                default:
                    throw new IllegalArgumentException("Sort must be EVENT_DATE or VIEWS");
            }
            events = eventRepository.findAll(specification, PageRequest.of(from / size, size, eventSort))
                    .getContent().stream()
                    .peek(event -> event.setConfirmedRequests(confirmedRequests.getOrDefault(event.getId(), 0L)))
                    .collect(Collectors.toList());
        } else {
            events = eventRepository.findAll(specification, PageRequest.of(from / size, size))
                    .getContent().stream()
                    .peek(event -> event.setConfirmedRequests(confirmedRequests.getOrDefault(event.getId(), 0L)))
                    .collect(Collectors.toList());
        }
        Set<String> uris = new HashSet<>();
        for (Event event : events) {
            uris.add("/events/" + event.getId());
        }
        List<ViewStats> stats = statsClient.getStats(
                "1970-01-01 00:00:00",
                "2999-12-31 23:59:59",
                uris,
                true
        ).getBody();
        if (stats != null) {
            for (Event event : events) {
                event.setViews(stats.stream()
                        .filter(vs -> vs.getUri().equals("/events/" + event.getId()))
                        .findFirst()
                        .map(ViewStats::getHits)
                        .orElse(0L));
            }
        }
        statsClient.addHit(EndpointHitDto.builder()
                .app(appName)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
        log.info("Возвращен список событий: {}", events);

        return events.stream().map(EventMapper::toFullDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getPublishedEventById(long id, HttpServletRequest request) {
        Event event = getEvent(id);
        if (event.getState() != State.PUBLISHED) {
            throw new NotFoundException("Event with id=" + id + " was not found");
        }
        String uri = request.getRequestURI();
        statsClient.addHit(EndpointHitDto.builder()
                .app(appName)
                .uri(uri)
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
        List<ViewStats> stats = statsClient.getStats(
                "1970-01-01 00:00:00",
                "2999-12-31 23:59:59",
                Collections.singleton(uri),
                true
        ).getBody();
        if (stats != null) {
            event.setViews(stats.stream()
                    .filter(vs -> vs.getUri().equals(uri))
                    .findFirst()
                    .map(ViewStats::getHits)
                    .orElse(0L));
        }
        log.info("Возвращено событие: {}", event);

        return EventMapper.toFullDto(event);
    }

    @Override
    public List<EventShortDto> getUserEvents(long userId, int from, int size) {
        getUser(userId);
        Map<Long, Long> confirmedRequests = requestRepository.findAllEventConfirmedRequests().stream()
                .collect(Collectors.toMap(EventConfirmedRequests::getEventId, EventConfirmedRequests::getCount));
        List<Event> events = eventRepository.findAllByInitiatorId(userId, PageRequest.of(from / size, size))
                .getContent().stream()
                .peek(event -> event.setConfirmedRequests(confirmedRequests.getOrDefault(event.getId(), 0L)))
                .collect(Collectors.toList());
        log.info("Возвращен список событий: {}", events);

        return events.stream().map(EventMapper::toShortDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto addNewEvent(long userId, NewEventDto newEventDto) {
        Event event = EventMapper.fromDto(newEventDto);
        event.setCategory(getCategory(newEventDto.getCategory()));
        event.setInitiator(getUser(userId));
        Event newEvent = eventRepository.save(event);
        log.info("Добавлено событие: {}", newEvent);

        return EventMapper.toFullDto(newEvent);
    }

    @Override
    public EventFullDto getUserEventById(long userId, long eventId) {
        getUser(userId);
        Event event = getEvent(eventId);
        checkEventInitiator(userId, event);
        log.info("Возвращено событие: {}", event);
        EventFullDto eventDto = EventMapper.toFullDto(event);
        eventDto.setConfirmedRequests(requestRepository.findEventConfirmedRequests(eventId));

        return eventDto;
    }

    @Override
    @Transactional
    public EventFullDto updateUserEvent(long userId, long eventId, UpdateEventUserRequest updateRequest) {
        getUser(userId);
        Event event = getEvent(eventId);
        checkEventInitiator(userId, event);
        checkEventNotPublished(event);
        updateEvent(event, updateRequest);
        StateActionUser stateAction = updateRequest.getStateAction();
        if (stateAction != null) {
            if (stateAction == StateActionUser.SEND_TO_REVIEW) {
                event.setState(State.PENDING);
            } else if (stateAction == StateActionUser.CANCEL_REVIEW) {
                event.setState(State.CANCELED);
            }
        }
        log.info("Обновлено событие: {}", event);

        return EventMapper.toFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getUserEventRequests(long userId, long eventId) {
        getUser(userId);
        Event event = getEvent(eventId);
        checkEventInitiator(userId, event);
        List<Request> requests = requestRepository.findAllByEventId(eventId);
        log.info("Возвращен список запросов: {}", requests);

        return requests.stream().map(RequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateUserEventRequestsStatus(long userId,
                                                                        long eventId,
                                                                        EventRequestStatusUpdateRequest updateRequest) {
        getUser(userId);
        Event event = getEvent(eventId);
        List<Request> requests = requestRepository.findAllById(updateRequest.getRequestIds());
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        for (Request request : requests) {
            if (request.getStatus() != Status.PENDING) {
                throw new IllegalOperationException("Request must have status PENDING");
            }
            if (updateRequest.getStatus() == Status.REJECTED) {
                request.setStatus(Status.REJECTED);
                result.getRejectedRequests().add(RequestMapper.toDto(request));
            } else {
                if (event.getParticipantLimit() == 0 || event.getConfirmedRequests() < event.getParticipantLimit()) {
                    request.setStatus(Status.CONFIRMED);
                    result.getConfirmedRequests().add(RequestMapper.toDto(request));
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                } else {
                    throw new IllegalOperationException("The participant limit has been reached");
                }
            }
        }
        log.info("Возвращен результат обновления статусов запросов: {}", result);

        return result;
    }

    private Event getEvent(long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        event.setConfirmedRequests(requestRepository.findEventConfirmedRequests(eventId));

        return event;
    }

    private User getUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
    }

    private Category getCategory(long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
    }

    private void checkEventInitiator(long userId, Event event) {
        if (event.getInitiator().getId() != userId) {
            throw new NotFoundException("Event with id=" + event.getId() + " was not found");
        }
    }

    private void checkEventNotPublished(Event event) {
        if (event.getState() == State.PUBLISHED) {
            throw new IllegalOperationException("Only pending or canceled events can be changed");
        }
    }

    private Specification<Event> getEventQuery(Set<Long> users,
                                               Set<String> states,
                                               Set<Long> categories,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd) {
        return (event, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (users != null && !users.isEmpty()) {
                predicates.add(event.get("initiator").get("id").in(users));
            }
            if (states != null && !states.isEmpty()) {
                Set<State> statesSet = states.stream().map(State::valueOf).collect(Collectors.toSet());
                predicates.add(event.get("state").in(statesSet));
            }
            if (categories != null && !categories.isEmpty()) {
                predicates.add(event.get("category").get("id").in(categories));
            }
            if (rangeStart != null && rangeEnd != null) {
                if (!rangeStart.isBefore(rangeEnd)) {
                    throw new IllegalArgumentException("Start date must be before end date.");
                }
                predicates.add(builder.between(event.get("eventDate"), rangeStart, rangeEnd));
            } else if (rangeStart != null) {
                predicates.add(builder.greaterThanOrEqualTo(event.get("eventDate"), rangeStart));
            } else if (rangeEnd != null) {
                predicates.add(builder.lessThanOrEqualTo(event.get("eventDate"), rangeEnd));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Specification<Event> getPublishedEventQuery(String text,
                                                        Set<Long> categories,
                                                        Boolean paid,
                                                        LocalDateTime rangeStart,
                                                        LocalDateTime rangeEnd,
                                                        boolean onlyAvailable) {
        return (event, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(event.get("state"), State.PUBLISHED));
            if (text != null) {
                predicates.add(builder.like(
                        builder.lower(event.get("annotation")),
                        builder.lower(builder.literal("%" + text + "%"))
                ));
            }
            if (categories != null && !categories.isEmpty()) {
                predicates.add(event.get("category").get("id").in(categories));
            }
            if (paid != null) {
                predicates.add(builder.equal(event.get("paid"), paid));
            }

            if (rangeStart != null && rangeEnd != null) {
                if (!rangeStart.isBefore(rangeEnd)) {
                    throw new IllegalArgumentException("Start date must be before end date.");
                }
                predicates.add(builder.between(event.get("eventDate"), rangeStart, rangeEnd));
            } else if (rangeStart != null) {
                predicates.add(builder.greaterThanOrEqualTo(event.get("eventDate"), rangeStart));
            } else if (rangeEnd != null) {
                predicates.add(builder.lessThanOrEqualTo(event.get("eventDate"), rangeEnd));
            } else {
                predicates.add(builder.greaterThan(event.get("eventDate"), LocalDateTime.now()));
            }
            if (onlyAvailable) {
                predicates.add(builder.or(
                        builder.equal(event.get("confirmedRequests"), 0),
                        builder.lessThan(event.get("confirmedRequests"), event.get("participantLimit"))
                ));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private <T extends UpdateEventRequest> void updateEvent(Event event, T request) {
        String annotation = request.getAnnotation();
        Long category = request.getCategory();
        String description = request.getDescription();
        LocalDateTime eventDate = request.getEventDate();
        Location location = request.getLocation();
        Boolean paid = request.getPaid();
        Integer participantLimit = request.getParticipantLimit();
        Boolean requestModeration = request.getRequestModeration();
        String title = request.getTitle();
        if (annotation != null && !annotation.isBlank()) {
            event.setAnnotation(annotation);
        }
        if (category != null) {
            event.setCategory(getCategory(category));
        }
        if (description != null && !description.isBlank()) {
            event.setDescription(description);
        }
        if (eventDate != null) {
            if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
                throw new IllegalArgumentException("The event date and time cannot be earlier than an hour from the current moment.");
            }
            event.setEventDate(eventDate);
        }
        if (location != null) {
            event.setLat(location.getLat());
            event.setLon(location.getLon());
        }
        if (paid != null) {
            event.setPaid(paid);
        }
        if (participantLimit != null) {
            event.setParticipantLimit(participantLimit);
        }
        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }
        if (title != null && !title.isBlank()) {
            event.setTitle(title);
        }
    }
}