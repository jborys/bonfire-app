package com.rbc.coaching.bonfireeventapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BonfireEventController.class)
public class BonfireEventControllerTest {

    private static ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BonfireEventService bonfireEventService;

    @Test
    public void shouldCreateEventSuccessfully() throws Exception {

        mockMvc.perform(post("/events")
                        .contentType("application/json")
                        .content(creatAnEvent()))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldRejectEventWithoutId() throws Exception {
        doThrow(new Exception())
                .when(bonfireEventService).createEvent(any());

        mockMvc.perform(post("/events")
                        .contentType("application/json")
                        .content(createInvalidEvent()))
                .andExpect(status().is4xxClientError());

        verify(bonfireEventService).createEvent(any());
    }

    @Test
    public void shouldRejectEventInThePast() throws Exception {
        mockMvc.perform(post("/events")
                        .contentType("application/json")
                        .content(createEventInThePast()))
                .andExpect(status().is4xxClientError());
    }



    private static String creatAnEvent() throws JsonProcessingException {
        BonfireEvent event = BonfireEvent.builder()
                .eventId(1234)
                .eventDate(LocalDate.now().plusDays(5))
                .build();
        return objectMapper.writeValueAsString(event);
    }

    private static String createInvalidEvent() throws JsonProcessingException {
        BonfireEvent event = BonfireEvent.builder()
                .build();
        return objectMapper.writeValueAsString(event);
    }

    private static String createEventInThePast() throws JsonProcessingException {
        BonfireEvent event = BonfireEvent.builder()
                .eventId(1234)
                .eventDate(LocalDate.now().minusDays(5))
                .build();
        return objectMapper.writeValueAsString(event);
    }
}
