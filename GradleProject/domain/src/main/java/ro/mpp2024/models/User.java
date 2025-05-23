package ro.mpp2024.models;
import jakarta.persistence.*;
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING)
public class User extends Identifiable<Integer> {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
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
