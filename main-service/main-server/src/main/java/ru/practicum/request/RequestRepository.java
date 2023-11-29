package ru.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.EventConfirmedRequests;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequesterId(Long requesterId);

    List<Request> findAllByEventId(Long eventId);

    @Query("select count(r) " +
            "from Request as r " +
            "where r.status = 'CONFIRMED' and r.event.id = ?1")
    Long findEventConfirmedRequests(Long eventId);

    @Query("select new ru.practicum.event.EventConfirmedRequests(r.event.id, count(r)) " +
            "from Request as r " +
            "where r.status = 'CONFIRMED' " +
            "group by r.event.id")
    List<EventConfirmedRequests> findAllEventConfirmedRequests();
}