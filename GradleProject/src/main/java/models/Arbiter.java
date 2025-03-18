package models;

public class Arbiter extends User {
    public Arbiter(String name, String password) {
        super(name, password, Role.REFEREE);
    }
}
