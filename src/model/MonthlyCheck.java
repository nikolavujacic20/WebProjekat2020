package model;

public class MonthlyCheck {
    public String name;
    public double price;

    public MonthlyCheck(String name, double price) {
        super();
        this.name = name;
        this.price = price;
    }

    public MonthlyCheck() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void add(double p) {
        price = price + p;
    }

}
