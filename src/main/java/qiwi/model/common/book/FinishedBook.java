package qiwi.model.common.book;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.sql.Date;

@MappedSuperclass
public class FinishedBook extends Book {
    private Date start;
    private Date end;
    @Column(name = "start_description")
    private String startDescription;
    @Column(name = "end_description")
    private String endDescription;

    public FinishedBook() {
    }

    public FinishedBook(String author, String name, Date start, Date end, String startDescription, String endDescription,
                        Date found, String foundDescription) {
        this.setName(name);
        this.setAuthor(author);
        this.setStart(start);
        this.setEnd(end);
        this.setStartDescription(startDescription);
        this.setEndDescription(endDescription);
        this.setFound(found);
        this.setFoundDescription(foundDescription);
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public String getStartDescription() {
        return startDescription;
    }

    public String getEndDescription() {
        return endDescription;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setStartDescription(String startDescription) {
        this.startDescription = startDescription;
    }

    public void setEndDescription(String endDescription) {
        this.endDescription = endDescription;
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
}
