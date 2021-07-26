package qiwi.model.english;

import qiwi.model.common.book.FinishedBook;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "`finished_books_english`")
public class FinishedBookEnglish extends FinishedBook {
    @OneToMany(mappedBy = "finishedBook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdditionalDatesEnglish> additionalDates;

    public FinishedBookEnglish() {
        super();
    }

    public FinishedBookEnglish(String author, String name, Date start, Date end, Date found, String foundDescription) {
        super(author, name, start, end, found, foundDescription);
    }
}
