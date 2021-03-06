package qiwi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import qiwi.model.AdditionalDate;
import qiwi.model.book.FinishedBook;
import qiwi.repository.FinishedBookRepository;
import qiwi.service.BookService;
import qiwi.util.enums.Language;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FinishedBookServiceImpl implements BookService<FinishedBook> {
    @Autowired
    private FinishedBookRepository repository;

    public List<FinishedBook> findAllByOrderByIdAsc(Language language) {
        return repository
                .findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .filter(b -> b.getLanguage().equals(language.firstLetterToUpperCase()))
                .collect(Collectors.toList());
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
    public void deleteBookById(Integer id) {
        repository.deleteById(id);
    }

    /*
     * Removes all the books relating to the specified language
     * */
    @Override
    public void clearLanguage(Language language) {
        for (FinishedBook book : repository.findAll())
            if (book.getLanguage().equals(language.firstLetterToUpperCase()))
                if (repository.existsById(book.getId()))
                    repository.deleteById(book.getId());
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

    public FinishedBook get(FinishedBook book) {
        return repository.findAll().contains(book) ? repository.findAll().get(repository.findAll().indexOf(book)) : null;
    }

    /*
     * Returns all the additional dates that books contain
     * */
    public List<AdditionalDate> getAllAdditionalDates(List<FinishedBook> books) {
        return books
                .stream()
                .filter(b -> b.getAdditionalDates().size() != 0)
                .map(FinishedBook::getAdditionalDates)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<FinishedBook> findAll() {
        return repository.findAll();
    }
}
