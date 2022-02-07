package qiwi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import qiwi.model.AdditionalDate;

public interface AdditionalDatesRepository extends JpaRepository<AdditionalDate, Integer> {
}
