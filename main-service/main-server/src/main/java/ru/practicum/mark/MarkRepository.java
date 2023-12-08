package ru.practicum.mark;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarkRepository extends JpaRepository<Mark, Long> {
    Page<Mark> findAllByUserId(Long userId, Pageable page);

    Page<Mark> findAllByEventId(Long eventId, Pageable page);
}