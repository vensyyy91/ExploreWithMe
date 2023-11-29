package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.enums.State;
import ru.practicum.enums.Status;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.IllegalOperationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getUserRequests(long userId) {
        getUser(userId);
        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        log.info("Возвращен список запросов: {}", requests);

        return requests.stream().map(RequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto addNewRequest(long userId, long eventId) {
        User requester = getUser(userId);
        Event event = getEvent(eventId);
        if (event.getInitiator().getId() == userId) {
            throw new IllegalOperationException("Event initiator cannot add a request to participate in his own event");
        }
        if (event.getState() != State.PUBLISHED) {
            throw new IllegalOperationException("Event must be published to add a request.");
        }
        if (event.getParticipantLimit() > 0 && event.getConfirmedRequests() == event.getParticipantLimit()) {
            throw new IllegalOperationException("The event has full participant limit.");
        }
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(requester)
                .status(!event.getRequestModeration() || event.getParticipantLimit() == 0 ? Status.CONFIRMED : Status.PENDING)
                .build();
        Request newRequest = requestRepository.save(request);
        if (request.getStatus() == Status.CONFIRMED) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }
        log.info("Добавлен запрос: {}", newRequest);

        return RequestMapper.toDto(newRequest);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        getUser(userId);
        Request request = getRequest(requestId);
        request.setStatus(Status.CANCELED);
        log.info("Отменен запрос: {}", request);

        return RequestMapper.toDto(request);
    }

    private User getUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
    }

    private Event getEvent(long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        event.setConfirmedRequests(requestRepository.findEventConfirmedRequests(eventId));

        return event;
    }

    private Request getRequest(long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id=" + requestId + " was not found"));
    }
}