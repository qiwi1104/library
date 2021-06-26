package qiwi.repository.russian;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import qiwi.model.russian.FinishedBookRussian;

@Service
public interface BookRussianRepository extends JpaRepository<FinishedBookRussian, Integer> {
}
