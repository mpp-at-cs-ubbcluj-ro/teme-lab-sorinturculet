package models;

public class User
{
    private int id;
    private String password;
    private Role role;

    public User(int id, String password, Role role)
    {

        this.id = id;
        this.password = password;
        this.role = role;
    }

}
