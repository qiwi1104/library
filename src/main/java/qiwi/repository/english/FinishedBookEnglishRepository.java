package qiwi.repository.english;

import org.springframework.stereotype.Service;
import qiwi.model.english.FinishedBookEnglish;
import qiwi.repository.common.FinishedBookRepository;

@Service
public interface FinishedBookEnglishRepository extends FinishedBookRepository<FinishedBookEnglish> {
}