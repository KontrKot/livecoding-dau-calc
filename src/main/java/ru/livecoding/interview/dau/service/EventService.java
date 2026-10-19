package ru.livecoding.interview.dau.service;

import ru.livecoding.interview.dau.controller.dto.ClickEventDto;
import ru.livecoding.interview.dau.controller.dto.DAUResponseDto;

public interface EventService {

    void postEvent(ClickEventDto event);

    DAUResponseDto getDAU(int[] authorsList);
}
