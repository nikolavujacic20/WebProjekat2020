package model;

public class DiscSearch {
    public String name;
    public int from;
    public int to;

    public DiscSearch() {
        super();
    }

    public DiscSearch(String name, int from, int to) {
        super();
        this.name = name;
        this.from = from;
        this.to = to;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }


}
