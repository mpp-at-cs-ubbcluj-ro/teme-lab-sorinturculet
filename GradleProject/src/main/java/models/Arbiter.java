package models;

public class Arbiter extends User {
    public Arbiter(String name, String password,Event event) {
        super(name, password, Role.REFEREE,event);
    }
}
