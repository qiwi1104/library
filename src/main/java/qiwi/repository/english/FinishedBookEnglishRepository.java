package qiwi.repository.english;

import org.springframework.stereotype.Service;
import qiwi.model.english.FinishedBookEnglish;
import qiwi.repository.common.FinishedBookRepository;

import java.util.List;

@Service
public interface FinishedBookEnglishRepository extends FinishedBookRepository<FinishedBookEnglish> {
    List<FinishedBookEnglish> findAllByOrderByIdAsc();
    List<FinishedBookEnglish> findAllByOrderByIdDesc();

    List<FinishedBookEnglish> findAllByOrderByStartAsc();
    List<FinishedBookEnglish> findAllByOrderByStartDesc();

    List<FinishedBookEnglish> findAllByOrderByEndAsc();
    List<FinishedBookEnglish> findAllByOrderByEndDesc();
}