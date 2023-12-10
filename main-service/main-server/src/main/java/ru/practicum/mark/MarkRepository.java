package ru.practicum.mark;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.EventRating;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MarkRepository extends JpaRepository<Mark, Long> {
    Page<Mark> findAllByUserId(Long userId, Pageable page);

    Page<Mark> findAllByEventId(Long eventId, Pageable page);

    @Query("select avg(m.mark) from Mark as m where m.event.id = ?1")
    Optional<Double> getEventRating(Long eventId);

    @Query("select new ru.practicum.event.EventRating(m.event.id, avg(m.mark)) " +
            "from Mark as m " +
            "where m.event.id in ?1 " +
            "group by m.event.id")
    List<EventRating> getAllEventsRating(Set<Long> ids);

    Optional<Mark> findByUserIdAndEventId(Long userId, Long eventId);
}