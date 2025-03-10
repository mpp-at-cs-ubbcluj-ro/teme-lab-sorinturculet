package models;

public class Participant {
    private final String name;

    public Participant(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public String toString() {
        return name + " (ID: " + id + ")";
    }
}