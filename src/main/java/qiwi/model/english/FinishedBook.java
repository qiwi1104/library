package qiwi.model.english;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "`finished_books_english`")
public class FinishedBook extends qiwi.model.FinishedBook {
    public FinishedBook() {super();}
    public FinishedBook(String author, String name, Date start, Date end, String startDescription, String endDescription,
                        Date found, String foundDescription) {
        super(author, name, start, end, startDescription, endDescription, found, foundDescription);
    }
}
