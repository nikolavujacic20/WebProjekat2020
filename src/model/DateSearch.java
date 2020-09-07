package model;

import java.util.Date;

public class DateSearch {
    public Date start;
    public Date end;

    public DateSearch() {
        super();
    }

    public DateSearch(Date start, Date end) {
        super();
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

}
