package qiwi.repository.common;

import org.springframework.data.jpa.repository.JpaRepository;
import qiwi.model.common.AdditionalDates;

public interface AdditionalDatesRepository<T extends AdditionalDates> extends JpaRepository<T, Integer> {
}
