package ru.practicum.mark;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.enums.Status;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.IllegalOperationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.Request;
import ru.practicum.request.RequestRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MarkServiceImpl implements MarkService {
    private final MarkRepository markRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    public List<MarkDto> getUserMarks(long userId, int from, int size) {
        getUser(userId);
        List<Mark> userMarks = markRepository.findAllByUserId(userId, PageRequest.of(from / size, size)).getContent();
        log.info("Возвращен список оценок: {}", userMarks);

        return userMarks.stream().map(MarkMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MarkDto addMark(long userId, long eventId, NewMarkDto newMarkDto) {
        Optional<Mark> oldMark = markRepository.findByUserIdAndEventId(userId, eventId);
        if (oldMark.isPresent()) {
            throw new IllegalOperationException("User with id=" + userId + " already rated the event with id=" + eventId);
        }
        User user = getUser(userId);
        Event event = getEvent(eventId);
        checkUserAndEvent(user, event);
        Mark mark = MarkMapper.fromDto(newMarkDto, user, event);
        Mark savedMark = markRepository.save(mark);
        log.info("Добавлена оценка: {}", savedMark);

        return MarkMapper.toDto(savedMark);
    }

    @Override
    @Transactional
    public MarkDto updateMark(long userId, long markId, UpdateMarkRequest request) {
        User user = getUser(userId);
        Mark mark = getMark(markId);
        if (!Objects.equals(mark.getUser(), user)) {
            throw new IllegalOperationException("User can update only his own mark");
        }
        if (request.getMark() != null) {
            mark.setMark(request.getMark());
        }
        if (request.getMessage() != null && !request.getMessage().isBlank()) {
            mark.setMessage(request.getMessage());
        }
        mark.setMarkedOn(LocalDateTime.now());
        log.info("Обновлена оценка: {}", mark);

        return MarkMapper.toDto(mark);
    }

    @Override
    @Transactional
    public void deleteMark(long userId, long markId) {
        User user = getUser(userId);
        Mark mark = getMark(markId);
        if (!Objects.equals(mark.getUser(), user)) {
            throw new IllegalOperationException("User can delete only his own mark");
        }
        markRepository.delete(mark);
        log.info("Удалена оценка с id={}", markId);
    }

    @Override
    public List<MarkDto> getRecentMarks(int from, int size) {
        List<Mark> marks = markRepository.findAll(PageRequest.of(
                from / size,
                size,
                Sort.by(Sort.Direction.DESC, "markedOn")
        )).getContent();
        log.info("Возвращен список оценок: {}", marks);

        return marks.stream().map(MarkMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<MarkDto> getAllEventMarks(long eventId, int from, int size) {
        getEvent(eventId);
        List<Mark> eventMarks = markRepository.findAllByEventId(eventId, PageRequest.of(from / size, size))
                .getContent();
        log.info("Возвращен список оценок: {}", eventMarks);

        return eventMarks.stream().map(MarkMapper::toDto).collect(Collectors.toList());
    }

    private Event getEvent(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
    }

    private User getUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
    }

    private Mark getMark(long markId) {
        return markRepository.findById(markId)
                .orElseThrow(() -> new NotFoundException("Mark with id=" + markId + " was not found"));
    }

    private void checkUserAndEvent(User user, Event event) {
        if (Objects.equals(event.getInitiator(), user)) {
            throw new IllegalOperationException("Event initiator cannot rate his own event");
        }
        Request request = requestRepository.findByRequesterIdAndEventId(user.getId(), event.getId())
                .orElseThrow(() -> new IllegalOperationException("User can rate only visited events"));
        if (request.getStatus() != Status.CONFIRMED) {
            throw new IllegalOperationException("User can rate only visited events");
        }
    }
}