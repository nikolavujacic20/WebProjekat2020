package model;

public abstract class Resource {
    protected String name;

    public Resource(String name) {
        super();
        this.name = name;
    }

    public Resource() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
