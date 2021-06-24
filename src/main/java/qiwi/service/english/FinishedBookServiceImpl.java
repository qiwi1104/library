package qiwi.service.english;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qiwi.model.english.FinishedBook;
import qiwi.repository.english.FinishedBookRepository;
import qiwi.service.BookService;

import java.util.List;

@Service
public class FinishedBookServiceImpl implements BookService<FinishedBook> {
    @Autowired
    private FinishedBookRepository finishedBookRepository;

    @Override
    public void addBook(FinishedBook book) {
        finishedBookRepository.save(book);
    }

    @Override
    public void deleteBook(Integer id) {
        finishedBookRepository.deleteById(id);
    }

    @Override
    public FinishedBook getBookById(Integer id) {
        return finishedBookRepository.getOne(id);
    }

    @Override
    public List<FinishedBook> findAll() {
        return finishedBookRepository.findAll();
    }
}
