package qiwi.model.english;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "`finished_books_english`")
public class FinishedBook extends qiwi.model.common.book.FinishedBook {
    @OneToMany(mappedBy = "finishedBook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdditionalDates> additionalDates;

    public FinishedBook() {
        super();
    }

    public FinishedBook(String author, String name, Date start, Date end, String startDescription, String endDescription,
                        Date found, String foundDescription) {
        super(author, name, start, end, startDescription, endDescription, found, foundDescription);
    }
}
