package qiwi.repository.english;

import qiwi.model.english.FinishedBookEnglish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface FinishedBookRepository extends JpaRepository<FinishedBookEnglish, Integer> {

}