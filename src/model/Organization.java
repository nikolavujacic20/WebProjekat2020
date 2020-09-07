package model;

import java.util.ArrayList;

public class Organization {
    private String name; // jedinstveno
    private String description;
    private String imagePath;

    private transient ArrayList<User> users = new ArrayList<User>();
    private transient ArrayList<Resource> resources = new ArrayList<Resource>();

    public Organization(String name, String description, String imagePath, ArrayList<User> users,
                        ArrayList<Resource> resources) {
        super();
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.users = users;
        this.resources = resources;
    }

    public Organization() {
        super();
    }

    public Organization(String string) {
        this.name = string;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(String name) {
        for (User u : this.users) {
            if (u.getName().equals(name)) {
                this.users.remove(u);
                break;
            }
        }
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public void setResources(ArrayList<Resource> resources) {
        this.resources = resources;
    }

    public void addResource(Resource resource) {
        this.resources.add(resource);
    }

    public void removeResource(String name) {
        for (Resource r : this.resources) {
            if (r.getName().equals(name)) {
                this.resources.remove(r);
                break;
            }
        }
    }


}
