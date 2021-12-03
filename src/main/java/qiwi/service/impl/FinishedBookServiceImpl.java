package qiwi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import qiwi.model.book.FinishedBook;
import qiwi.repository.FinishedBookRepository;
import qiwi.service.BookService;

import java.util.List;

@Service
public class FinishedBookServiceImpl implements BookService<FinishedBook> {
    @Autowired
    private FinishedBookRepository repository;

    public List<FinishedBook> findAllByOrderByIdAsc() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public List<FinishedBook> findAllByOrderByIdDesc() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public List<FinishedBook> findAllByOrderByStartAsc() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "start"));
    }

    public List<FinishedBook> findAllByOrderByStartDesc() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "start"));
    }

    public List<FinishedBook> findAllByOrderByEndAsc() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "end"));
    }

    public List<FinishedBook> findAllByOrderByEndDesc() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "end"));
    }

    public List<FinishedBook> findAllByOrderByFoundByIdAsc() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "found", "id"));
    }

    public List<FinishedBook> findAllByOrderByFoundByIdDesc() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "found", "id"));
    }

    @Override
    public void addBook(FinishedBook book) {
        repository.save(book);
    }

    @Override
    public void addAll(List<FinishedBook> bookList) {
        repository.saveAll(bookList);
    }

    @Override
    public void deleteBook(Integer id) {
        repository.deleteById(id);

        // Deleted book was not the last one in the list
        if (id != repository.findAll().size() + 1) {
            // Computing new IDs for the books whose IDs were greater than that of the deleted book
            repository.computeIds();
        }
    }

    @Override
    public void clearAll() {
        repository.deleteAll();
    }

    @Override
    public FinishedBook getBookById(Integer id) {
        if (id <= repository.count())
            return repository.getOne(id);
        else return null;
    }

    @Override
    public boolean exists(FinishedBook book) {
        for (FinishedBook finishedBook : repository.findAll()) {
            if (finishedBook.equals(book)) {
                return true;
            }
        }

        return false;
    }

    public FinishedBook get(FinishedBook book) {
        for (FinishedBook finishedBook : repository.findAll()) {
            if (finishedBook.equals(book)) {
                return finishedBook;
            }
        }

        return null;
    }

    @Override
    public List<FinishedBook> findAll() {
        return repository.findAll();
    }
}
