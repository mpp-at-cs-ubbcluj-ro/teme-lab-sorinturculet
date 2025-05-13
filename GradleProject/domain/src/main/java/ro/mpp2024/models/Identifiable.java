package ro.mpp2024.models;

import java.io.Serializable;

import jakarta.persistence.*;
@MappedSuperclass
public class Identifiable<ID extends Serializable> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
