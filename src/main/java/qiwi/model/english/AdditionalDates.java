package qiwi.model.english;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "`additional_dates_english`")
public class AdditionalDates extends qiwi.model.AdditionalDates {
    public AdditionalDates() {super();}

    public AdditionalDates(Integer id, Date start, Date end) {super(id, start, end);}
}
