package ro.mpp2024.models;
import jakarta.persistence.*;
@Entity
@DiscriminatorValue("Arbiter")
public class Arbiter extends User {
    public Arbiter(String name, String password,Event event) {
        super(name, password, Role.REFEREE,event);
    }

    public Arbiter() {
        super();
    }
}
