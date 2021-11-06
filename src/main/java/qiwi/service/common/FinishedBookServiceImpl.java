package qiwi.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import qiwi.model.common.book.FinishedBook;
import qiwi.repository.common.FinishedBookRepository;
import qiwi.service.BookService;

import java.util.List;

@Service
public abstract class FinishedBookServiceImpl<
        T extends FinishedBook,
        S extends FinishedBookRepository<T>> implements BookService<T> {
    @Autowired
    private S repository;

    public List<T> findAllByOrderByIdAsc() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public List<T> findAllByOrderByIdDesc() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public List<T> findAllByOrderByStartAsc() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "start"));
    }

    public List<T> findAllByOrderByStartDesc() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "start"));
    }

    public List<T> findAllByOrderByEndAsc() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "end"));
    }

    public List<T> findAllByOrderByEndDesc() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "end"));
    }

    public List<T> findAllByOrderByFoundByIdAsc() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "found", "id"));
    }

    public List<T> findAllByOrderByFoundByIdDesc() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "found", "id"));
    }

    @Override
    public void addBook(T book) {
        repository.save(book);
    }

    @Override
    public void addAll(List<T> bookList) {
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
    public T getBookById(Integer id) {
        if (id <= repository.count())
            return repository.getOne(id);
        else return null;
    }

    @Override
    public boolean exists(T book) {
        for (T finishedBook : repository.findAll()) {
            if (finishedBook.equals(book)) {
                return true;
            }
        }

        return false;
    }

    public T get(T book) {
        for (T finishedBook : repository.findAll()) {
            if (finishedBook.equals(book)) {
                return finishedBook;
            }
        }

        return null;
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }
}
