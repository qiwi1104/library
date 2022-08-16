package qiwi.service.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import qiwi.model.book.FinishedBook;
import qiwi.model.enums.Language;
import qiwi.repository.FinishedBookRepository;
import qiwi.service.BookService;

import java.util.List;
import java.util.Optional;

@Service
public class FinishedBookDAO implements BookService<FinishedBook> {
    @Autowired
    private FinishedBookRepository repository;

    @Override
    public void addBook(FinishedBook book) {
        repository.save(book);
    }

    @Override
    public void addAll(List<FinishedBook> bookList) {
        repository.saveAll(bookList);
    }

    @Override
    public void deleteBookById(Integer id) {
        repository.deleteById(id);
    }

    /*
     * Removes all the books relating to the specified language
     * */
    @Override
    public void clearLanguage(Language language) {
        for (FinishedBook book : repository.findAll())
            if (book.getLanguage().equals(language.firstLetterToUpperCase())) {
                if (repository.existsById(book.getId())) {
                    repository.deleteById(book.getId());
                }
            }
    }

    @Override
    public FinishedBook getBookById(Integer id) {
        Optional<FinishedBook> book = repository.findById(id);
        return book.orElse(null);
    }

    @Override
    public boolean exists(FinishedBook book) {
        return repository.findAll().contains(book);
    }

    @Override
    public List<FinishedBook> findAll() {
        return repository.findAll();
    }

    public List<FinishedBook> findAll(Sort sort) {
        return repository.findAll(sort);
    }
}
