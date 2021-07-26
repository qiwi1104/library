package qiwi.model.common.book;

import javax.persistence.MappedSuperclass;
import java.sql.Date;

@MappedSuperclass
public class FinishedBook extends Book implements Cloneable {
    private Date start;
    private Date end;

    public FinishedBook() {
    }

    public FinishedBook(String author, String name, Date start, Date end, Date found, String description) {
        this.setName(name);
        this.setAuthor(author);
        this.setStart(start);
        this.setEnd(end);
        this.setFound(found);
        this.setDescription(description);
    }

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
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
