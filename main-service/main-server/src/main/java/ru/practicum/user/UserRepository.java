package ru.practicum.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select new ru.practicum.user.Initiator(u.id, u.name, u.email, avg(m.mark)) " +
            "from User as u " +
            "inner join Event as e on u.id = e.initiator.id " +
            "join Mark as m on e.id = m.event.id " +
            "group by u.id " +
            "order by avg(m.mark) desc")
    Page<Initiator> findTopInitiators(Pageable page);
}