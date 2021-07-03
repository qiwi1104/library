package qiwi.repository.russian;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import qiwi.model.russian.FinishedBookRussian;

import java.util.List;

@Service
public interface FinishedBookRussianRepository extends JpaRepository<FinishedBookRussian, Integer> {
    List<FinishedBookRussian> findAllByOrderByIdAsc();
    List<FinishedBookRussian> findAllByOrderByIdDesc();

    List<FinishedBookRussian> findAllByOrderByStartAsc();
    List<FinishedBookRussian> findAllByOrderByStartDesc();

    List<FinishedBookRussian> findAllByOrderByEndAsc();
    List<FinishedBookRussian> findAllByOrderByEndDesc();
}