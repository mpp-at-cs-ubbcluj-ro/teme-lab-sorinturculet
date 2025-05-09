package ro.mpp2024.models;

public class User extends Identifiable<Integer> {
    private String name;
    private String password;
    private Role role;
    private Event event;

    // Add no-arg constructor for Gson
    public User() {
    }

    public User(String name, String password, Role role,Event event) {
        this.name = name;
        this.password = password;
        this.role = role;
        this.event = event;
    }
    public User(String name, String password) {
        this.name = name;
        this.password = password;
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
