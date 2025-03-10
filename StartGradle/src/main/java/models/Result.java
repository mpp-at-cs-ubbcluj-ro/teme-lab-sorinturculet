package com.triathlon.model;

public class Result {
    private Participant participant;
    private String event;
    private int points;

    public Result(Participant participant, String event, int points) {
        this.participant = participant;
        this.event = event;
        this.points = points;
    }

    public Participant getParticipant() {
        return participant;
    }

    public String getEvent() {
        return event;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return participant.getName() + " (ID: " + participant.getId() + ") - Event: " + event + ", Points: " + points;
    }
}
