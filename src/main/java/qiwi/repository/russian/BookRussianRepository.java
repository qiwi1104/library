package qiwi.repository.russian;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import qiwi.model.russian.BookRussian;

@Service
public interface BookRussianRepository extends JpaRepository<BookRussian, Integer> {
}
