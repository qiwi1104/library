package qiwi.model.spanish;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import qiwi.model.common.book.FinishedBook;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "`finished_books_spanish`")
@JsonAutoDetect
public class FinishedBookSpanish extends FinishedBook {
    @JsonProperty("additional_dates")
    @OneToMany(mappedBy = "finishedBook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdditionalDatesSpanish> additionalDates;

    @Override
    public List<AdditionalDatesSpanish> getAdditionalDates() {
        return additionalDates;
    }
}
