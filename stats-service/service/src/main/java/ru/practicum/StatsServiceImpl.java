package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public EndpointHitDto addHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = EndpointHitMapper.fromDto(endpointHitDto);
        EndpointHit newEndpointHit = statsRepository.save(endpointHit);
        log.info("Возвращен объект: {}", newEndpointHit);

        return EndpointHitMapper.toDto(newEndpointHit);
    }

    @Override
    public List<ViewStats> getStats(String start, String end, Set<String> uris, boolean unique) {
        LocalDateTime startTime = LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8), formatter);
        LocalDateTime endTime = LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8), formatter);
        List<ViewStats> stats = unique
                ? statsRepository.getStatsWithUnique(startTime, endTime)
                : statsRepository.getStatsWithoutUnique(startTime, endTime);
        if (uris != null && !uris.isEmpty()) {
            stats = stats.stream()
                    .filter(stat -> uris.contains(stat.getUri()))
                    .collect(Collectors.toList());
        }
        log.info("Возвращен список статистики: {}", stats);

        return stats;
    }
}