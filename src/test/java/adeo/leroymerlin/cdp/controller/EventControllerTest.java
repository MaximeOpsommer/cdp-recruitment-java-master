package adeo.leroymerlin.cdp.controller;

import adeo.leroymerlin.cdp.entity.Event;
import adeo.leroymerlin.cdp.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventControllerTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void findEvents() throws Exception {
        mvc.perform(get("/api/events/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    public void updateEvent() throws Exception {
        Long eventId = 1000L;
        Event expected = new Event();
        expected.setId(eventId);
        expected.setNbStars(5);
        mvc.perform(put("/api/events/" + eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expected)))
                .andExpect(status().isOk());

        Event actual = eventRepository.findOne(eventId);
        assertThat(actual.getNbStars()).isEqualTo(5);
    }

    @Test
    public void deleteEvent() throws Exception {
        Long eventId = 1000L;
        mvc.perform(delete(("/api/events/" + eventId)))
                .andExpect(status().isOk());

        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSize(4);
        assertThat(events.stream().map(Event::getId).collect(Collectors.toList())).doesNotContain(eventId);
    }

    @Test
    public void findEventsWithQuery() throws Exception {
        mvc.perform(get("/api/events/search/Wa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1000)));
    }
}