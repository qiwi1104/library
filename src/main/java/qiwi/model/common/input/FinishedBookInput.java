package qiwi.model.common.input;

import java.sql.Date;

public class FinishedBookInput extends Input {
    protected Date start = Date.valueOf("1970-1-1");
    protected Date end = Date.valueOf("1970-1-1");

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
