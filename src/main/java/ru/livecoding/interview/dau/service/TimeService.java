package ru.livecoding.interview.dau.service;

import java.time.Instant;

public interface TimeService {
    // Возвращает Instant на начало текущих суток
    Instant today();
    Instant yesterday();
}
