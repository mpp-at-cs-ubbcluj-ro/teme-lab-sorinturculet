package models;

public class Arbiter extends User {
    public Arbiter(int id, String password) {
        super(id, password, Role.REFEREE);
    }
}