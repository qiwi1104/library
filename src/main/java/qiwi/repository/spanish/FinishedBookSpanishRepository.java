package qiwi.repository.spanish;

import org.springframework.stereotype.Service;
import qiwi.model.spanish.FinishedBookSpanish;
import qiwi.repository.common.FinishedBookRepository;

import java.util.List;

@Service
public interface FinishedBookSpanishRepository extends FinishedBookRepository<FinishedBookSpanish> {
    List<FinishedBookSpanish> findAllByOrderByIdAsc();
    List<FinishedBookSpanish> findAllByOrderByIdDesc();

    List<FinishedBookSpanish> findAllByOrderByStartAsc();
    List<FinishedBookSpanish> findAllByOrderByStartDesc();

    List<FinishedBookSpanish> findAllByOrderByEndAsc();
    List<FinishedBookSpanish> findAllByOrderByEndDesc();
}