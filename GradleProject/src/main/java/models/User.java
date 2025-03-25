package models;

public class User extends Identifiable<Integer> {
    private String name;
    private String password;
    private Role role;
    private Event event;

    public User(String name, String password, Role role,Event event) {
        this.name = name;
        this.password = password;
        this.role = role;
        this.event = event;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Event getEvent(){return event;}
}
