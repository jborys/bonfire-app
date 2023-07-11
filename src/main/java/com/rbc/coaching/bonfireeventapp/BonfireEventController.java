package com.rbc.coaching.bonfireeventapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;

@Controller
public class BonfireEventController {

    @Autowired
    private BonfireEventService bonfireEventService;

    @PostMapping("/events")
    public ResponseEntity<String> createEvents(@RequestBody BonfireEvent event) {
        try {
            bonfireEventService.createEvent(event);
        } catch (Exception e) {
        }
        if (eventHasNoId(event) || eventIsInThePast(event)) {
            return buildResponse(HttpStatus.BAD_REQUEST);
        }
        return buildResponse(HttpStatus.CREATED);
    }

    private static boolean eventIsInThePast(BonfireEvent event) {
        return event.getEventDate().isBefore(LocalDate.now());
    }

    private static boolean eventHasNoId(BonfireEvent event) {
        return event.getEventId() == null;
    }

    private static ResponseEntity<String> buildResponse(HttpStatus status) {
        return ResponseEntity.status(status).build();
    }
}
