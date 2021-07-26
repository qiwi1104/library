package qiwi.model.spanish;

import qiwi.model.common.book.FinishedBook;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "`finished_books_spanish`")
public class FinishedBookSpanish extends FinishedBook {
    @OneToMany(mappedBy = "finishedBook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdditionalDatesSpanish> additionalDates;

    public FinishedBookSpanish() {
        super();
    }

    public FinishedBookSpanish(String author, String name, Date start, Date end, Date found, String description) {
        super(author, name, start, end, found, description);
    }
}
