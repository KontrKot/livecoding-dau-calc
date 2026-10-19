package ru.livecoding.interview.dau;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import ru.livecoding.interview.dau.controller.dto.ClickEventDto;
import ru.livecoding.interview.dau.controller.dto.DAUResponseDto;
import ru.livecoding.interview.dau.controller.dto.UserWithInstant;
import ru.livecoding.interview.dau.service.EventService;
import ru.livecoding.interview.dau.service.TimeService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SpringBootTest
class DauApplicationTests {

    @Autowired
    EventService eventService;

    @Autowired
    TimeService timeService;

    @Test
    void postEventWithoutAuthorInMapTest() {
        ClickEventDto clickEventDto = new ClickEventDto(123, 1);
        Assertions.assertDoesNotThrow(() -> eventService.postEvent(clickEventDto));
        Map<Integer, Set<UserWithInstant>> res = (Map<Integer, Set<UserWithInstant>>) ReflectionTestUtils.getField(eventService, "eventsMap");

        Assertions.assertEquals(1, res.size());
        Assertions.assertTrue(res.containsKey(clickEventDto.authorId()));
        Assertions.assertEquals(1, res.get(clickEventDto.authorId()).size());
        UserWithInstant resUserSet = res.get(clickEventDto.authorId()).iterator().next();

        Assertions.assertEquals(clickEventDto.userId(), resUserSet.getUserId());
    }

    @Test
    void postEventWithAuthorInMapTest() {
        int authorId = 1;
        Map<Integer, Set<UserWithInstant>> usersMap = new HashMap<>();
        Set<UserWithInstant> set = new HashSet<>();
        set.add(new UserWithInstant(124, timeService.today()));
        usersMap.put(authorId, set);
        ReflectionTestUtils.setField(eventService, "eventsMap", usersMap);


        ClickEventDto clickEventDto = new ClickEventDto(123, authorId);
        Assertions.assertDoesNotThrow(() -> eventService.postEvent(clickEventDto));
        Map<Integer, Set<UserWithInstant>> res = (Map<Integer, Set<UserWithInstant>>) ReflectionTestUtils.getField(eventService, "eventsMap");

        Assertions.assertEquals(1, res.size());
        Assertions.assertTrue(res.containsKey(clickEventDto.authorId()));
        Assertions.assertEquals(2, res.get(clickEventDto.authorId()).size());
        Set<UserWithInstant> resUserSet = res.get(clickEventDto.authorId());

        Assertions.assertTrue(resUserSet.stream().allMatch(el -> Set.of(123, 124).contains(el.getUserId())));
    }

    @Test
    void getDauWithAllAuthorsPresent() {
        Map<Integer, Set<UserWithInstant>> usersMap = new HashMap<>();
        Set<UserWithInstant> setFirstAuthor = new HashSet<>();
        setFirstAuthor.add(new UserWithInstant(120, timeService.yesterday()));
        setFirstAuthor.add(new UserWithInstant(121, timeService.yesterday()));
        setFirstAuthor.add(new UserWithInstant(122, timeService.yesterday()));
        setFirstAuthor.add(new UserWithInstant(123, timeService.yesterday()));
        usersMap.put(1, setFirstAuthor);

        Set<UserWithInstant> setSecondAuthor = new HashSet<>();
        setSecondAuthor.add(new UserWithInstant(12, timeService.yesterday()));
        setSecondAuthor.add(new UserWithInstant(13, timeService.yesterday()));
        setSecondAuthor.add(new UserWithInstant(14, timeService.yesterday()));
        usersMap.put(2, setSecondAuthor);

        Set<UserWithInstant> setThirdAuthor = new HashSet<>();
        setThirdAuthor.add(new UserWithInstant(12, timeService.yesterday()));
        usersMap.put(3, setThirdAuthor);

        ReflectionTestUtils.setField(eventService, "eventsMap", usersMap);


        DAUResponseDto responseDto = eventService.getDAU(new int[] {1, 2, 3});

        Assertions.assertEquals(3, responseDto.uniqueUsersCount().length);

        Assertions.assertEquals(4, responseDto.uniqueUsersCount()[0]);
        Assertions.assertEquals(3, responseDto.uniqueUsersCount()[1]);
        Assertions.assertEquals(1, responseDto.uniqueUsersCount()[2]);
    }

    @Test
    void getDauNotAllAuthorsPresent() {
        Map<Integer, Set<UserWithInstant>> usersMap = new HashMap<>();

        Set<UserWithInstant> setSecondAuthor = new HashSet<>();
        setSecondAuthor.add(new UserWithInstant(12, timeService.yesterday()));
        setSecondAuthor.add(new UserWithInstant(13, timeService.yesterday()));
        setSecondAuthor.add(new UserWithInstant(14, timeService.yesterday()));
        setSecondAuthor.add(new UserWithInstant(15, timeService.yesterday()));
        setSecondAuthor.add(new UserWithInstant(16, timeService.yesterday()));
        usersMap.put(2, setSecondAuthor);

        ReflectionTestUtils.setField(eventService, "eventsMap", usersMap);


        DAUResponseDto responseDto = eventService.getDAU(new int[] {1, 2, 3});

        Assertions.assertEquals(3, responseDto.uniqueUsersCount().length);

        Assertions.assertEquals(0, responseDto.uniqueUsersCount()[0]);
        Assertions.assertEquals(5, responseDto.uniqueUsersCount()[1]);
        Assertions.assertEquals(0, responseDto.uniqueUsersCount()[2]);
    }

    @Test
    void getDauNotAllAuthorsPresentAndNonUniqueRequestsTest() {
        Map<Integer, Set<UserWithInstant>> usersMap = new HashMap<>();

        Set<UserWithInstant> setSecondAuthor = new HashSet<>();
        setSecondAuthor.add(new UserWithInstant(12, timeService.yesterday()));
        setSecondAuthor.add(new UserWithInstant(12, timeService.yesterday()));
        setSecondAuthor.add(new UserWithInstant(12, timeService.yesterday()));
        setSecondAuthor.add(new UserWithInstant(15, timeService.yesterday()));
        setSecondAuthor.add(new UserWithInstant(16, timeService.yesterday()));
        usersMap.put(2, setSecondAuthor);

        ReflectionTestUtils.setField(eventService, "eventsMap", usersMap);


        DAUResponseDto responseDto = eventService.getDAU(new int[] {1, 2, 3});

        Assertions.assertEquals(3, responseDto.uniqueUsersCount().length);

        Assertions.assertEquals(0, responseDto.uniqueUsersCount()[0]);
        Assertions.assertEquals(3, responseDto.uniqueUsersCount()[1]);
        Assertions.assertEquals(0, responseDto.uniqueUsersCount()[2]);
    }

}
