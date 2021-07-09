package qiwi.model.russian;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "`finished_books_russian`")
public class FinishedBookRussian extends qiwi.model.common.book.FinishedBook {
    @OneToMany(mappedBy = "finishedBook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdditionalDatesRussian> additionalDates;

    public FinishedBookRussian() {
        super();
    }

    public FinishedBookRussian(String author, String name, Date start, Date end, String startDescription, String endDescription,
                               Date found, String foundDescription) {
        super(author, name, start, end, startDescription, endDescription, found, foundDescription);
    }
}
