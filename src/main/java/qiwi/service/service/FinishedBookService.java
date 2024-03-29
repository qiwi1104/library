package qiwi.service.service;

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
import qiwi.service.dao.FinishedBookDAO;
import qiwi.util.JSONHandler;
import qiwi.validator.FinishedBookValidator;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static qiwi.model.enums.BookType.FINISHED;
import static qiwi.model.enums.SortBy.START;
import static qiwi.model.enums.SortType.ASC;
import static qiwi.model.enums.SortType.DESC;

@Service
public class FinishedBookService {
    @Autowired
    private FinishedBookDAO finishedBookDAO;
    private final FinishedBookValidator validator = new FinishedBookValidator();

    private SortType sortDateMethod = ASC;
    private SortBy sortProperty = START;

    public boolean addBook(FinishedBook book, BindingResult result, Model model, Language language) {
        validator.validate(book, result);

        if (result.hasErrors()) {
            setUpView(model, language);

            return false;
        }

        if (finishedBookDAO.exists(book)) {
            FinishedBook bookFromLibrary = get(book);
            AdditionalDate additionalDate = new AdditionalDate();

            additionalDate.setStart(book.getStart());
            additionalDate.setEnd(book.getEnd());
            additionalDate.setFinishedBook(book);

            if (!bookFromLibrary.hasDate(additionalDate)) {
                bookFromLibrary.addDate(additionalDate);

                finishedBookDAO.addBook(bookFromLibrary);

                return true;
            } else {
                setUpView(model, language);
                result.reject("alreadyExists", "This book already exists.");

                return false;
            }
        }

        finishedBookDAO.addBook(book);

        return true;
    }

    public boolean editBook(FinishedBook book, BindingResult result, Model model, Language language) {
        validator.validate(book, result);

        if (result.hasErrors()) {
            setUpView(model, language);

            return false;
        }

        if (finishedBookDAO.exists(book)) {
            FinishedBook bookFromLibrary = finishedBookDAO.getBookById(book.getId());
            AdditionalDate additionalDate = new AdditionalDate();

            additionalDate.setStart(book.getStart());
            additionalDate.setEnd(book.getEnd());

            if (!bookFromLibrary.hasDate(additionalDate)) {
                bookFromLibrary.setStart(book.getStart());
                bookFromLibrary.setEnd(book.getEnd());
                bookFromLibrary.setFound(book.getFound());

                finishedBookDAO.addBook(bookFromLibrary);

                return true;
            } else {
                setUpView(model, language);

                result.reject("alreadyExists", "This book already exists.");

                return false;
            }
        } else {
            finishedBookDAO.addBook(book);

            return true;
        }
    }

    public FinishedBook get(FinishedBook book) {
        return finishedBookDAO.findAll().contains(book)
                ? finishedBookDAO.findAll().get(finishedBookDAO.findAll().indexOf(book))
                : null;
    }

    public FinishedBook getBookById(Integer id) {
        return finishedBookDAO.getBookById(id);
    }

    public void deleteBookById(Integer id) {
        finishedBookDAO.deleteBookById(id);
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

    public List<FinishedBook> findAllByOrderByIdAsc(Language language) {
        return finishedBookDAO
                .findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .filter(b -> b.getLanguage().equals(language.firstLetterToUpperCase()))
                .collect(Collectors.toList());
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

    @Transactional
    public void load(PathDTO input, Language language) {
        List<FinishedBook> books = JSONHandler.IO.readJSONFile(input.getPath(), FINISHED, language);

        if (books.size() != 0) {
            finishedBookDAO.clearLanguage(language);
            finishedBookDAO.addAll(books);
        }
    }

    @Transactional
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
