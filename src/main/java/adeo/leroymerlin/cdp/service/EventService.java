package adeo.leroymerlin.cdp.service;

import adeo.leroymerlin.cdp.entity.Event;
import adeo.leroymerlin.cdp.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getEvents() {
        return eventRepository.findAllBy();
    }

    @Transactional
    public void delete(Long id) {
        eventRepository.delete(id);
    }

    @Transactional
    public void update(Long id, Event event) {
        Event existingEvent = eventRepository.findOne(id);
        if (existingEvent == null) {
            System.err.println("Event with id " + id + " does not exist");
        }
        if (!id.equals(event.getId())) {
            System.err.println("Given id and event id are not matching");
        }
        eventRepository.save(event);
    }

    @Transactional
    public List<Event> getFilteredEvents(String query) {
        return eventRepository.findAllBy().stream()
                .filter(event -> event.getBands()
                        .stream()
                        .anyMatch(band -> band.getMembers()
                                .stream()
                                .anyMatch(member -> member.getName().contains(query))
                        )
                ).collect(Collectors.toList());
    }
}
