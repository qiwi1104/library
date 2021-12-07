package qiwi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import qiwi.model.book.FinishedBook;
import qiwi.repository.FinishedBookRepository;
import qiwi.service.BookService;
import qiwi.util.enums.Language;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinishedBookServiceImpl implements BookService<FinishedBook> {
    @Autowired
    private FinishedBookRepository repository;

    public List<FinishedBook> findAllByOrderByIdAsc() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public List<FinishedBook> findAllByOrderByIdAsc(Language language) {
        return repository
                .findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .filter(b -> b.getLanguage().equals(language.firstLetterToUpperCase()))
                .collect(Collectors.toList());
    }

    /*
     * Removes all the books relating to the specified language
     * */
    public void clearLanguage(Language language) {
        for (FinishedBook book : repository.findAll())
            if (book.getLanguage().equals(language.firstLetterToUpperCase()))
                if (repository.existsById(book.getId()))
                    repository.deleteById(book.getId());
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
        try {
            repository.saveAll(bookList);
        } catch (Exception e) {
            repository.saveAll(bookList);
        }
    }

    @Override
    public void deleteBook(Integer id) {
        repository.deleteById(id);
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
        return repository.findAll().contains(book);
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
