package ru.livecoding.interview.dau.service.impl;

import org.springframework.stereotype.Service;
import ru.livecoding.interview.dau.controller.dto.ClickEventDto;
import ru.livecoding.interview.dau.controller.dto.DAUResponseDto;
import ru.livecoding.interview.dau.controller.dto.UserWithInstant;
import ru.livecoding.interview.dau.service.EventService;
import ru.livecoding.interview.dau.service.TimeService;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EventServiceImpl implements EventService {

    private final Map<Integer, Set<UserWithInstant>> eventsMap;
    private final TimeService timeService;

    public EventServiceImpl(TimeService timeService) {
        this.eventsMap = new ConcurrentHashMap<>();
        this.timeService = timeService;
    }

    @Override
    public void postEvent(ClickEventDto event) {
        if (eventsMap.containsKey(event.authorId())) {
            eventsMap.get(event.authorId()).add(new UserWithInstant(event.userId(), timeService.today()));
        } else {
            Set<UserWithInstant> userIds = ConcurrentHashMap.newKeySet();
            userIds.add(new UserWithInstant(event.userId(), timeService.today()));
            eventsMap.put(event.authorId(), userIds);
        }
    }

    @Override
    public DAUResponseDto getDAU(int[] authorsList) {
        int[] uniqueUsersCount = new int[authorsList.length];
        for (int i = 0; i < authorsList.length; i++) {
            int authorId = authorsList[i];
            Set<UserWithInstant> userWithInstants = eventsMap.get(authorId);
            if (userWithInstants == null) {
                uniqueUsersCount[i] = 0;
            } else {
                userWithInstants.removeIf(userWithInstant -> isBefore(userWithInstant, timeService.yesterday()));
                long count = userWithInstants.stream().filter(userWithInstant -> isBefore(userWithInstant, timeService.today())).count();
                uniqueUsersCount[i] = (int) count;
            }
        }

        return new DAUResponseDto(uniqueUsersCount);
    }

    private boolean isBefore(UserWithInstant userWithInstant, Instant compareTo) {
        return userWithInstant.getTimeOfRequest().isBefore(compareTo);
    }
}
