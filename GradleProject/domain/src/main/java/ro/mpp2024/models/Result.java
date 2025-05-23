package ro.mpp2024.models;
import jakarta.persistence.*;
@Entity
@Table(name = "results")
public class Result extends Identifiable<Integer> {
    @ManyToOne(optional = false)
    @JoinColumn(name = "participant_id")
    private Participant participant;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Event event;
    @Column(nullable = false)
    private int points;

    public Result(Participant participant, Event event, int points) {
        this.participant = participant;
        this.event = event;
        this.points = points;
    }

    public Result() {

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
