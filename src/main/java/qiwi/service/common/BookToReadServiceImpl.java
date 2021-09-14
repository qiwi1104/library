package qiwi.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import qiwi.model.common.book.BookToRead;
import qiwi.repository.common.BookToReadRepository;
import qiwi.service.BookService;

import java.util.List;

public abstract class BookToReadServiceImpl<T extends BookToRead, S extends BookToReadRepository<T>> implements BookService<T> {
    @Autowired
    private S repository;

    public List<T> findAllByOrderByIdAsc() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public List<T> findAllByOrderByIdDesc() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
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
    public void addAll(List<T> booksList) {
        repository.saveAll(booksList);
    }

    @Override
    public void deleteBook(Integer id) {
        repository.deleteById(id);

        if (id != repository.findAll().size() + 1) { // deleted book was not the last one in the list
            /*
             * Computing new IDs for the books whose IDs were greater than that of the deleted book
             * */
            if (id != repository.findAll().size()) {
                for (int i = id + 1; i < repository.findAll().size() + 1; i++) {
                    T book = (T) repository.getOne(i).clone();

                    book.setId(i - 1);
                    repository.save(book);
                }
            } else {
                T book = (T) repository.getOne(repository.findAll().size() + 1).clone();

                book.setId(repository.findAll().size());
                repository.save(book);
            }
            /*
             * The last book in the list and the previous one now have identical values
             * */
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
