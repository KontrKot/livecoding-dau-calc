package ru.livecoding.interview.dau.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;
import ru.livecoding.interview.dau.service.TimeService;

@Component
public class TimeServiceImpl implements TimeService {
    @Override
    public Instant today() {
        return Instant.now().truncatedTo(ChronoUnit.DAYS);
    }

    public Instant yesterday() {
        return Instant.now().minus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
    }
}
