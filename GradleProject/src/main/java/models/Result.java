package models;

public class Result extends Identifiable<Integer> {
    private Participant participant;
    private Event event;
    private int points;

    public Result(Participant participant, Event event, int points) {
        this.participant = participant;
        this.event = event;
        this.points = points;
    }

    public Participant getParticipant() {
        return participant;
    }

    public Event getEvent() {
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
