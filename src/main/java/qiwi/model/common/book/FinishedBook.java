package qiwi.model.common.book;

import qiwi.controllers.common.BookController;
import qiwi.model.common.AdditionalDates;

import javax.persistence.MappedSuperclass;
import java.sql.Date;
import java.util.List;
import java.util.Map;

@MappedSuperclass
public class FinishedBook extends Book implements Cloneable {
    private Date start;
    private Date end;

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    protected Map<String, String> setKeys() {
        Map<String, String> keys = super.setKeys();

        String endStr = BookController.TimeFormat.formatTime("yyyy-M-d", "M/d/yy", end.toString());
        String startStr = BookController.TimeFormat.formatTime("yyyy-M-d", "M/d/yy", start.toString());

        keys.putAll(Map.of(
                "start", startStr,
                "end", endStr
        ));

        return keys;
    }

    @Override
    public String toString() {
        Map<String, String> keys = setKeys();

        return "{" +
                "\"author\": \"" + author + "\"" +
                ", \"name\": \"" + keys.get("name") + "\"" +
                ", \"found\": \"" + keys.get("found") + "\"" +
                ", \"description\": \"" + keys.get("description") + "\"" +
                ", \"dates\": [\"" + keys.get("start") + "\", " + "\"" + keys.get("end") + "\"]" +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        FinishedBook book = (FinishedBook) o;

        return name.equals(book.name) && author.equals(book.author);
    }

    @Override
    public FinishedBook clone() {
        try {
            return (FinishedBook) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return new FinishedBook();
    }
}
