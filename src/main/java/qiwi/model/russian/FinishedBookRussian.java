package qiwi.model.russian;

import qiwi.model.common.book.FinishedBook;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "`finished_books_russian`")
public class FinishedBookRussian extends FinishedBook {
    @OneToMany(mappedBy = "finishedBook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdditionalDatesRussian> additionalDates;

    public FinishedBookRussian() {
        super();
    }

    public FinishedBookRussian(String author, String name, Date start, Date end, Date found, String foundDescription) {
        super(author, name, start, end, found, foundDescription);
    }
}
