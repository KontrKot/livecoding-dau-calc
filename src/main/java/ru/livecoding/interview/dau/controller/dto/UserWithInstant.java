package ru.livecoding.interview.dau.controller.dto;

import java.time.Instant;
import java.util.Objects;

public class UserWithInstant {

    private final int userId;

    private final Instant timeOfRequest;

    public UserWithInstant(int userId, Instant timeOfRequest) {
        this.userId = userId;
        this.timeOfRequest = timeOfRequest;
    }

    public int getUserId() {
        return userId;
    }

    public Instant getTimeOfRequest() {
        return timeOfRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserWithInstant that = (UserWithInstant) o;
        return userId == that.userId && Objects.equals(timeOfRequest, that.timeOfRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, timeOfRequest);
    }
}
