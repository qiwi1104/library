package qiwi.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import qiwi.dto.PathDTO;
import qiwi.model.book.BookToRead;
import qiwi.model.book.FinishedBook;
import qiwi.model.enums.Language;
import qiwi.model.enums.SortBy;
import qiwi.model.enums.SortType;
import qiwi.service.dao.BookToReadDAO;
import qiwi.service.dao.FinishedBookDAO;
import qiwi.util.JSONHandler;
import qiwi.validator.BookValidator;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static qiwi.model.enums.BookType.TO_READ;
import static qiwi.model.enums.SortBy.FOUND;
import static qiwi.model.enums.SortType.ASC;
import static qiwi.model.enums.SortType.DESC;

@Service
public class BookToReadService {
    @Autowired
    private BookToReadDAO bookToReadDAO;
    @Autowired
    private FinishedBookDAO finishedBookDAO;

    private final BookValidator validator = new BookValidator();

    private SortType sortDateMethod = ASC;
    private SortBy sortProperty = FOUND;

    public boolean addBook(BookToRead book, BindingResult result, Model model, Language language) {
        validator.validate(book, result);

        if (result.hasErrors()) {
            setUpView(model, language);

            return false;
        }

        if (bookToReadDAO.exists(book)) {
            setUpView(model, language);

            result.reject("alreadyExists", "This book already exists.");

            return false;
        }

        bookToReadDAO.addBook(book);

        return true;
    }

    public boolean editBook(BookToRead book, BindingResult result, Model model, Language language) {
        validator.validate(book, result);

        if (result.hasErrors()) {
            setUpView(model, language);

            return false;
        }

        if (bookToReadDAO.exists(book)) {
            setUpView(model, language);

            result.reject("alreadyExists", "This book already exists.");

            return false;
        }

        bookToReadDAO.addBook(book);

        return true;
    }

    @Transactional
    public void finishBook(FinishedBook book, Integer id) {
        finishedBookDAO.addBook(book);
        bookToReadDAO.deleteBookById(id);
    }

    @Transactional
    public void load(PathDTO input, Language language) {
        List<BookToRead> books = JSONHandler.IO.readJSONFile(input.getPath(), TO_READ, language);

        if (books.size() != 0) {
            bookToReadDAO.clearLanguage(language);
            bookToReadDAO.addAll(books);
        }
    }

    @Transactional
    public void loadBatch(PathDTO input, Language language) {
        List<BookToRead> books = JSONHandler.IO.readJSONFile(input.getPath(), TO_READ, language);

        if (books.size() != 0) {
            bookToReadDAO.addAll(books);
        }
    }

    @Transactional
    public void save(PathDTO input, Language language) {
        List<BookToRead> bookToReadList = findAllByOrderByIdAsc(language);
        JSONHandler.IO.saveTableToJSON(bookToReadList, input.getPath(), language, TO_READ);
    }

    public void sort(SortBy sortProperty) {
        sortDateMethod = sortDateMethod.equals(ASC) ? DESC : ASC;
        this.sortProperty = sortProperty;
    }

    public List<BookToRead> sortList(List<BookToRead> list) {
        switch (sortDateMethod) {
            case ASC:
                switch (sortProperty) {
                    case ID:
                        list.sort(Comparator.comparing(BookToRead::getId));
                        break;
                    case FOUND:
                        list.sort(Comparator.comparing(BookToRead::getFound).thenComparing(BookToRead::getId));
                        break;
                }
                break;
            case DESC:
                switch (sortProperty) {
                    case ID:
                        list.sort(Comparator.comparing(BookToRead::getId));
                        Collections.reverse(list);
                        break;
                    case FOUND:
                        list.sort(Comparator.comparing(BookToRead::getFound).thenComparing(BookToRead::getId));
                        Collections.reverse(list);
                        break;
                }
                break;
        }
        return list;
    }

    public List<BookToRead> findAllByOrderByIdAsc(Language language) {
        return bookToReadDAO
                .findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .filter(b -> b.getLanguage().equals(language.firstLetterToUpperCase()))
                .collect(Collectors.toList());
    }

    public void setUpView(Model model, Language language) {
        List<BookToRead> books = findAllByOrderByIdAsc(language);

        books = sortList(books);

        model.addAttribute("booksToRead", books);
    }
}
