package qiwi.repository.english;

import qiwi.model.english.FinishedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FinishedBookRepository extends JpaRepository<FinishedBook, Integer> {
    List<FinishedBook> findAllByOrderByIdAsc();
    List<FinishedBook> findAllByOrderByIdDesc();

    List<FinishedBook> findAllByOrderByStartAsc();
    List<FinishedBook> findAllByOrderByStartDesc();

    List<FinishedBook> findAllByOrderByEndAsc();
    List<FinishedBook> findAllByOrderByEndDesc();
}