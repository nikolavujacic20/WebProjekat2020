package model;

import java.util.Date;

public class Activity {
    private Date turnOn;
    private Date turnOff;


    public Activity() {
        super();
    }


    public Activity(Date turnOn) {
        super();
        this.turnOn = turnOn;
        this.turnOff = null;
    }


    public Activity(Date turnOn, Date turnOff) {
        super();
        this.turnOn = turnOn;
        this.turnOff = turnOff;
    }

    public Date getTurnOn() {
        return turnOn;
    }

    public void setTurnOn(Date turnOn) {
        this.turnOn = turnOn;
    }

    public Date getTurnOff() {
        return turnOff;
    }

    public void setTurnOff(Date turnOff) {
        this.turnOff = turnOff;
    }


}
