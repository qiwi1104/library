package qiwi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import qiwi.model.book.FinishedBook;

public interface FinishedBookRepository extends JpaRepository<FinishedBook, Integer> {
}
