package qiwi.service.english;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import qiwi.model.english.FinishedBook;
import qiwi.repository.english.FinishedBookRepository;
import qiwi.service.BookService;

import java.util.List;

@Service
public class FinishedBookServiceImpl implements BookService<FinishedBook> {
    @Autowired
    private FinishedBookRepository finishedBookRepository;

    public List<FinishedBook> findAllByOrderByIdAsc() {
        return finishedBookRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public List<FinishedBook> findAllByOrderByIdDesc() {
        return finishedBookRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public List<FinishedBook> findAllByOrderByStartAsc() {
        return finishedBookRepository.findAll(Sort.by(Sort.Direction.ASC, "start"));
    }

    public List<FinishedBook> findAllByOrderByStartDesc() {
        return finishedBookRepository.findAll(Sort.by(Sort.Direction.DESC, "start"));
    }

    public List<FinishedBook> findAllByOrderByEndAsc() {
        return finishedBookRepository.findAll(Sort.by(Sort.Direction.ASC, "end"));
    }

    public List<FinishedBook> findAllByOrderByEndDesc() {
        return finishedBookRepository.findAll(Sort.by(Sort.Direction.DESC, "end"));
    }

    @Override
    public void addBook(FinishedBook book) {
        finishedBookRepository.save(book);
    }

    @Override
    public void addAll(List<FinishedBook> bookList) {
        finishedBookRepository.saveAll(bookList);
    }

    @Override
    public void deleteBook(Integer id) {
        finishedBookRepository.deleteById(id);
    }

    @Override
    public void clearAll() {
        finishedBookRepository.deleteAll();
    }

    @Override
    public FinishedBook getBookById(Integer id) {
        return finishedBookRepository.getOne(id);
    }

    @Override
    public List<FinishedBook> findAll() {
        return finishedBookRepository.findAll();
    }

    public boolean isInTable(FinishedBook book) {
        for (FinishedBook finishedBook : finishedBookRepository.findAll()) {
            if (finishedBook.equals(book)) {
                book.setId(finishedBook.getId());
                book.setFound(finishedBook.getFound());
                return true;
            }
        }

        return false;
    }
}
