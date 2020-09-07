package model;

public class User {
    private String email; // jedinstveno
    private String name;
    private String lastName;
    private String password;
    private Organization organization;
    private Role role;

    public User() {
        super();
    }

    public User(String email, String name, String lastName, String password, Organization organization,
                Role role) {
        super();
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.password = password;
        this.organization = organization;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


}
