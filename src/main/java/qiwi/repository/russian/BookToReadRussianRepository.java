package qiwi.repository.russian;

import org.springframework.data.jpa.repository.JpaRepository;
import qiwi.model.russian.BookToReadRussian;

import java.util.List;

public interface BookToReadRussianRepository extends JpaRepository<BookToReadRussian, Integer> {
    List<BookToReadRussian> findAllByOrderByIdAsc();
    List<BookToReadRussian> findAllByOrderByIdDesc();

    List<BookToReadRussian> findAllByOrderByFoundAsc();
    List<BookToReadRussian> findAllByOrderByFoundDesc();
}
