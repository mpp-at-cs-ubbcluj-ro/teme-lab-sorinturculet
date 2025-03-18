package models;

import java.io.Serializable;

public class Identifiable<ID extends Serializable> implements Serializable {
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
