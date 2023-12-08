package ru.practicum.mark;

import java.util.List;

public interface MarkService {
    List<MarkDto> getUserMarks(long userId, int from, int size);

    MarkDto addMark(long userId, long eventId, NewMarkDto newMarkDto);

    MarkDto updateMark(long userId, long markId, UpdateMarkRequest updateMarkRequest);

    void deleteMark(long userId, long markId);

    List<MarkDto> getRecentMarks(int from, int size);

    List<MarkDto> getAllEventMarks(long eventId, int from, int size);
}