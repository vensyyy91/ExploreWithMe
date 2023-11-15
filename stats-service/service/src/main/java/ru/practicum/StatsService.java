package ru.practicum;

import java.util.List;
import java.util.Set;

public interface StatsService {
    EndpointHitDto addHit(EndpointHitDto endpointHitDto);

    List<ViewStats> getStats(String start, String end, Set<String> uris, boolean unique);
}