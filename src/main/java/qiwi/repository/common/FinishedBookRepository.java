package qiwi.repository.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import qiwi.model.common.book.FinishedBook;

public interface FinishedBookRepository<T extends FinishedBook> extends JpaRepository<T, Integer> {
    @Procedure
    void computeIds();
}
