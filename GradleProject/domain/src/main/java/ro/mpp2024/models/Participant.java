package ro.mpp2024.models;

import jakarta.persistence.*;

@Entity
@Table(name = "participants")
public class Participant extends Identifiable<Integer> {
    @Column(nullable = false)
    private String name;
    @Transient
    private int totalPoints; // Calculated

    public Participant(String name) {
        this.name = name;
        this.totalPoints = 0;
    }

    public Participant() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    @Override
    public String toString() {
        return name + " (" + totalPoints + " pts)";
    }
}
