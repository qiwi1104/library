package qiwi.service.russian;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import qiwi.model.russian.FinishedBookRussian;
import qiwi.repository.russian.FinishedBookRussianRepository;
import qiwi.service.BookService;

import java.util.List;

@Service
public class FinishedBookServiceRussianImpl implements BookService<FinishedBookRussian> {
    @Autowired
    private FinishedBookRussianRepository finishedBookRussianRepository;

    public List<FinishedBookRussian> findAllByOrderByIdAsc() {
        return finishedBookRussianRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public List<FinishedBookRussian> findAllByOrderByIdDesc() {
        return finishedBookRussianRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public List<FinishedBookRussian> findAllByOrderByStartAsc() {
        return finishedBookRussianRepository.findAll(Sort.by(Sort.Direction.ASC, "start"));
    }

    public List<FinishedBookRussian> findAllByOrderByStartDesc() {
        return finishedBookRussianRepository.findAll(Sort.by(Sort.Direction.DESC, "start"));
    }

    public List<FinishedBookRussian> findAllByOrderByEndAsc() {
        return finishedBookRussianRepository.findAll(Sort.by(Sort.Direction.ASC, "end"));
    }

    public List<FinishedBookRussian> findAllByOrderByEndDesc() {
        return finishedBookRussianRepository.findAll(Sort.by(Sort.Direction.DESC, "end"));
    }

    @Override
    public void addBook(FinishedBookRussian book) {
        finishedBookRussianRepository.save(book);
    }

    @Override
    public void addAll(List<FinishedBookRussian> bookList) {
        finishedBookRussianRepository.saveAll(bookList);
    }

    @Override
    public void deleteBook(Integer id) {
        finishedBookRussianRepository.deleteById(id);
    }

    @Override
    public void clearAll() {
        finishedBookRussianRepository.deleteAll();
    }

    @Override
    public FinishedBookRussian getBookById(Integer id) {
        return finishedBookRussianRepository.getOne(id);
    }

    @Override
    public List<FinishedBookRussian> findAll() {
        return finishedBookRussianRepository.findAll();
    }
}
