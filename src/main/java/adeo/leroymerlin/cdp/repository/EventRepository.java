package adeo.leroymerlin.cdp.repository;

import adeo.leroymerlin.cdp.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface EventRepository extends JpaRepository<Event, Long> {

    void delete(Long eventId);

    @Query("UPDATE Event " +
            "SET comment = :comment," +
            "nbStars = :nbStars " +
            "WHERE id = :eventId")
    void update(Long eventId, Integer nbStars, String comment);

    List<Event> findAllBy();
}
