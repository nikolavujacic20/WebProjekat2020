package model;

public class Disc extends Resource {

    private DiscType type;
    private int capacity; // u GB;
    private VM virtualMachine;

    public Disc(String name) {
        super(name);
    }

    public Disc(String name, DiscType type, int capacity, VM virtualMachine) {
        super(name);
        this.type = type;
        this.capacity = capacity;
        this.virtualMachine = virtualMachine;
    }

    public DiscType getType() {
        return type;
    }

    public void setType(DiscType type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public VM getVirtualMachine() {
        return virtualMachine;
    }

    public void setVirtualMachine(VM virtualMachine) {
        this.virtualMachine = virtualMachine;
    }


}
