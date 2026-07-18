package com.eventledger.eventgateway.controller;

import com.eventledger.eventgateway.client.AccountServiceClient;
import com.eventledger.eventgateway.dto.ErrorResponse;
import com.eventledger.eventgateway.dto.EventRequest;
import com.eventledger.eventgateway.dto.EventResponse;
import com.eventledger.eventgateway.service.EventGatewayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventGatewayController {

    private final EventGatewayService eventGatewayService;

    public EventGatewayController(EventGatewayService eventGatewayService) {
        this.eventGatewayService = eventGatewayService;
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody EventRequest request) {
        try {
            EventResponse response = eventGatewayService.createEvent(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("VALIDATION_ERROR", ex.getMessage(), 400));
        } catch (AccountServiceClient.ServiceUnavailableException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new ErrorResponse("SERVICE_UNAVAILABLE", ex.getMessage(), 503));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("INTERNAL_ERROR", ex.getMessage(), 500));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable String id) {
        try {
            EventResponse response = eventGatewayService.getEvent(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("NOT_FOUND", ex.getMessage(), 404));
        }
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getEventsByAccount(
            @RequestParam String account) {
        List<EventResponse> responses = eventGatewayService.getEventsByAccount(account);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}

