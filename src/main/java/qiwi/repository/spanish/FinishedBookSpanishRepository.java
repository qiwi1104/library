package qiwi.repository.spanish;

import org.springframework.stereotype.Service;
import qiwi.model.spanish.FinishedBookSpanish;
import qiwi.repository.common.FinishedBookRepository;

@Service
public interface FinishedBookSpanishRepository extends FinishedBookRepository<FinishedBookSpanish> {
}