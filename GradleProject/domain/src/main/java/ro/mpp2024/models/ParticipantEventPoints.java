package ro.mpp2024.models;

public class ParticipantEventPoints {
    private final String participantName;
    private final int points;
    private final int participantId;

    public ParticipantEventPoints(String participantName, int points, int participantId) {
        this.participantName = participantName;
        this.points = points;
        this.participantId = participantId;
    }

    public String getParticipantName() {
        return participantName;
    }

    public int getPoints() {
        return points;
    }

    public int getParticipantId() {
        return participantId;
    }

    @Override
    public String toString() {
        return participantName + " (ID: " + participantId + ") - " + points + " pts";
    }
}