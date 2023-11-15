package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select new ru.practicum.ViewStats(h.app, h.uri, count(h.ip)) " +
            "from EndpointHit as h " +
            "where h.timestamp between ?1 and ?2 " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<ViewStats> getStatsWithoutUnique(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.ViewStats(h.app, h.uri, count(distinct(h.ip))) " +
            "from EndpointHit as h " +
            "where h.timestamp between ?1 and ?2 " +
            "group by h.app, h.uri " +
            "order by count(distinct(h.ip)) desc")
    List<ViewStats> getStatsWithUnique(LocalDateTime start, LocalDateTime end);
}