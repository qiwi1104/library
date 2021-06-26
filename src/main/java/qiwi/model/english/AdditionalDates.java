package qiwi.model.english;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "`additional_dates_english`")
public class AdditionalDates extends qiwi.model.AdditionalDates {
    @ManyToOne
    @JoinColumn(name = "finished_book_id", insertable = false, updatable = false)
    private FinishedBook finishedBook;

    public AdditionalDates() {super();}

    public AdditionalDates(Integer id, Integer finishedBookId, Date start, Date end) {super(id, finishedBookId, start, end);}
}
