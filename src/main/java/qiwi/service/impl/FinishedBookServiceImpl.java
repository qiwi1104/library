package qiwi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import qiwi.dto.PathDTO;
import qiwi.model.AdditionalDate;
import qiwi.model.book.FinishedBook;
import qiwi.model.enums.Language;
import qiwi.model.enums.SortBy;
import qiwi.model.enums.SortType;
import qiwi.repository.FinishedBookRepository;
import qiwi.service.BookService;
import qiwi.util.JSONHandler;
import qiwi.validator.FinishedBookValidator;

import java.util.*;
import java.util.stream.Collectors;

import static qiwi.model.enums.BookType.FINISHED;
import static qiwi.model.enums.SortBy.START;
import static qiwi.model.enums.SortType.ASC;
import static qiwi.model.enums.SortType.DESC;

@Service
public class FinishedBookServiceImpl implements BookService<FinishedBook> {
    @Autowired
    private FinishedBookRepository repository;

    private final FinishedBookValidator validator = new FinishedBookValidator();

    private SortType sortDateMethod = ASC;
    private SortBy sortProperty = START;

    public List<FinishedBook> findAllByOrderByIdAsc(Language language) {
        return repository
                .findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .filter(b -> b.getLanguage().equals(language.firstLetterToUpperCase()))
                .collect(Collectors.toList());
    }

    public String addBook(FinishedBook book, BindingResult result, Model model, Language language) {
        validator.validate(book, result);

        if (result.hasErrors()) {
            setUpView(model, language);

            return "finished-books/" + language.toLowerCase() + "/add-book";
        }

        if (exists(book)) {
            FinishedBook bookFromLibrary = get(book);
            AdditionalDate additionalDate = new AdditionalDate();

            additionalDate.setStart(bookFromLibrary.getStart());
            additionalDate.setEnd(bookFromLibrary.getEnd());

            if (!book.hasDate(additionalDate)) {
                book.addDate(additionalDate);
                addBook(book);

                return "redirect:/finishedbooks/" + language.toLowerCase() + "/";
            } else {
                setUpView(model, language);
                result.reject("alreadyExists", "This book already exists.");

                return "finished-books/" + language.toLowerCase() + "/add-book";
            }
        }

        addBook(book);

        return "redirect:/finishedbooks/" + language.toLowerCase() + "/";
    }

    @Override
    public void addBook(FinishedBook book) {
        repository.save(book);
    }

    public String editBook(FinishedBook book, BindingResult result, Model model, Language language) {
        validator.validate(book, result);

        if (result.hasErrors()) {
            setUpView(model, language);

            return "finished-books/" + language.toLowerCase() + "/edit-book";
        }

        if (exists(book)) {
            FinishedBook bookFromLibrary = get(book);
            AdditionalDate additionalDate = new AdditionalDate();

            additionalDate.setStart(book.getStart());
            additionalDate.setEnd(book.getEnd());

            if (!bookFromLibrary.hasDate(additionalDate)) {
                bookFromLibrary.setStart(book.getStart());
                bookFromLibrary.setEnd(book.getEnd());
                bookFromLibrary.setFound(book.getFound());

                addBook(bookFromLibrary);

                return "redirect:/finishedbooks/" + language.toLowerCase() + "/";
            } else {
                setUpView(model, language);
                result.reject("alreadyExists", "This book already exists.");

                return "finished-books/" + language.toLowerCase() + "/edit-book";
            }
        }

        addBook(book);

        return "redirect:/finishedbooks/" + language.toLowerCase() + "/";
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

    public List<FinishedBook> sortList(List<FinishedBook> books) {
        switch (sortDateMethod) {
            case ASC:
                switch (sortProperty) {
                    case ID:
                        books.sort(Comparator.comparing(FinishedBook::getId));
                        break;
                    case START:
                        books.sort(Comparator.comparing(FinishedBook::getStart).thenComparing(FinishedBook::getId));
                        break;
                    case END:
                        books.sort(Comparator.comparing(FinishedBook::getEnd).thenComparing(FinishedBook::getId));
                        break;
                    case FOUND:
                        books.sort(Comparator.comparing(FinishedBook::getFound).thenComparing(FinishedBook::getId));
                        break;
                }
                break;
            case DESC:
                switch (sortProperty) {
                    case ID:
                        books.sort(Comparator.comparing(FinishedBook::getId));
                        Collections.reverse(books);
                        break;
                    case START:
                        books.sort(Comparator.comparing(FinishedBook::getStart).thenComparing(FinishedBook::getId));
                        Collections.reverse(books);
                        break;
                    case END:
                        books.sort(Comparator.comparing(FinishedBook::getEnd).thenComparing(FinishedBook::getId));
                        Collections.reverse(books);
                        break;
                    case FOUND:
                        books.sort(Comparator.comparing(FinishedBook::getFound).thenComparing(FinishedBook::getId));
                        Collections.reverse(books);
                        break;
                }
                break;
        }
        return books;
    }

    public void sort(SortBy sortProperty) {
        sortDateMethod = sortDateMethod.equals(ASC) ? DESC : ASC;
        this.sortProperty = sortProperty;
    }

    public void load(PathDTO input, Language language) {
        List<FinishedBook> books = JSONHandler.IO.readJSONFile(input.getPath(), FINISHED, language);

        if (books.size() != 0) {
            clearLanguage(language);
            addAll(books);
        }
    }

    public void save(PathDTO input, Language language) {
        List<FinishedBook> books = findAllByOrderByIdAsc(language);
        JSONHandler.IO.saveTableToJSON(books, input.getPath(), language, FINISHED);
    }

    public void setUpView(Model model, Language language) {
        List<FinishedBook> books = findAllByOrderByIdAsc(language);

        // Sorts according to current settings
        books = sortList(books);

        model.addAttribute("books", books);
        model.addAttribute("additionalDates", getAllAdditionalDates(books));
    }
}
