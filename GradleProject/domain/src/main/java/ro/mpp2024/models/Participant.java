package ro.mpp2024.models;

public class Participant extends Identifiable<Integer> {
    private String name;
    private int totalPoints; // Calculated

    public Participant(String name) {
        this.name = name;
        this.totalPoints = 0;
    }

    public String getName() {
        return name;
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
