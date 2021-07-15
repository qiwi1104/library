package qiwi.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import qiwi.model.common.book.BookToRead;
import qiwi.repository.common.BookToReadRepository;
import qiwi.service.BookService;

import java.util.List;

@Service
public abstract class BookToReadServiceImpl<T extends BookToRead, E extends BookToReadRepository<T>> implements BookService<T> {
    @Autowired
    private E repository;

    public List<T> findAllByOrderByIdAsc() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public List<T> findAllByOrderByIdDesc() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public List<T> findAllByOrderByFoundAsc() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "found"));
    }

    public List<T> findAllByOrderByFoundDesc() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "found"));
    }

    @Override
    public void addBook(T book) {
        repository.save(book);
    }

    @Override
    public void addAll(List<T> booksList) {
        repository.saveAll(booksList);
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
}
