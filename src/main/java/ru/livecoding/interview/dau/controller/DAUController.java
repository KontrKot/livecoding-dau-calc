package ru.livecoding.interview.dau.controller;

import org.springframework.web.bind.annotation.*;
import ru.livecoding.interview.dau.controller.dto.ClickEventDto;
import ru.livecoding.interview.dau.controller.dto.DAUResponseDto;
import ru.livecoding.interview.dau.service.EventService;

@RestController
public class DAUController {
    private final EventService eventService;

    public DAUController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/event")
    public void postEvent(@RequestBody ClickEventDto event) {
        eventService.postEvent(event);
    }

    @GetMapping("/dau")
    public DAUResponseDto getDAU(@RequestParam int[] authorsList) {
        return eventService.getDAU(authorsList);
    }
}
