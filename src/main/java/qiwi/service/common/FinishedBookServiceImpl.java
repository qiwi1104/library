package qiwi.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import qiwi.model.common.book.FinishedBook;
import qiwi.repository.common.FinishedBookRepository;
import qiwi.service.BookService;

import java.util.List;

@Service
public abstract class FinishedBookServiceImpl<T extends FinishedBook, E extends FinishedBookRepository<T>> implements BookService<T> {
    @Autowired
    private E repository;

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

        if (id != repository.findAll().size() + 1) {
            if (id != repository.findAll().size()) {
                for (int i = id + 1; i < repository.findAll().size() + 1; i++) {
                    T book = null;
                    try {
                        book = (T) repository.getOne(i).clone(); // без clone() выкидывает ошибку
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }

                    book.setId(i - 1);
                    repository.save(book);
                }
            } else {
                T book = null;
                try {
                    book = (T) repository.getOne(repository.findAll().size() + 1).clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

                book.setId(repository.findAll().size());
                repository.save(book);
            }
            repository.deleteById(repository.findAll().size());
        }
    }

    @Override
    public void clearAll() {
        repository.deleteAll();
    }

    @Override
    public T getBookById(Integer id) {
        return repository.getOne(id);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    public boolean isInTable(T book) {
        for (T finishedBook : repository.findAll()) {
            if (finishedBook.equals(book)) {
                book.setId(finishedBook.getId());
                book.setFound(finishedBook.getFound());
                return true;
            }
        }

        return false;
    }
}
