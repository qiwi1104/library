package qiwi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import qiwi.model.book.FinishedBook;

public interface FinishedBookRepository extends JpaRepository<FinishedBook, Integer> {
    @Procedure("computeIdsFinished")
    void computeIds();
}
