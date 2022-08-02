package qiwi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import qiwi.dto.PathDTO;
import qiwi.model.book.BookToRead;
import qiwi.model.enums.Language;
import qiwi.model.enums.SortBy;
import qiwi.model.enums.SortType;
import qiwi.repository.BookToReadRepository;
import qiwi.service.BookService;
import qiwi.util.JSONHandler;
import qiwi.validator.BookValidator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static qiwi.model.enums.BookType.TO_READ;
import static qiwi.model.enums.SortBy.FOUND;
import static qiwi.model.enums.SortType.ASC;
import static qiwi.model.enums.SortType.DESC;

@Service
public class BookToReadServiceImpl implements BookService<BookToRead> {
    @Autowired
    private BookToReadRepository repository;

    private final BookValidator validator = new BookValidator();

    private SortType sortDateMethod = ASC;
    private SortBy sortProperty = FOUND;

    public List<BookToRead> findAllByOrderByIdAsc(Language language) {
        return repository
                .findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .filter(b -> b.getLanguage().equals(language.firstLetterToUpperCase()))
                .collect(Collectors.toList());
    }

    public String addBook(BookToRead book, BindingResult result, Model model, Language language) {
        validator.validate(book, result);

        if (result.hasErrors()) {
            setUpView(model, language);

            return "books-to-read/" + language.toLowerCase() + "/add-book";
        }

        if (exists(book)) {
            setUpView(model, language);

            result.reject("alreadyExists", "This book already exists.");

            return "books-to-read/" + language.toLowerCase() + "/add-book";
        }

        repository.save(book);

        return "redirect:/bookstoread/" + language.toLowerCase() + "/";
    }

    @Override
    public void addBook(BookToRead book) {
        repository.save(book);
    }

    public String editBook(BookToRead book, BindingResult result, Model model, Language language) {
        validator.validate(book, result);

        if (result.hasErrors()) {
            setUpView(model, language);

            return "books-to-read/" + language.toLowerCase() + "/edit-book";
        }

        if (exists(book)) {
            setUpView(model, language);

            result.reject("alreadyExists", "This book already exists.");

            return "books-to-read/" + language.toLowerCase() + "/edit-book";
        }

        addBook(book);

        return "redirect:/bookstoread/" + language.toLowerCase() + "/";
    }

    @Override
    public void addAll(List<BookToRead> booksList) {
        repository.saveAll(booksList);
    }

    @Override
    public void deleteBookById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public void clearLanguage(Language language) {
        for (BookToRead book : repository.findAll())
            if (book.getLanguage().equals(language.firstLetterToUpperCase()))
                if (repository.existsById(book.getId()))
                    repository.deleteById(book.getId());
    }

    @Override
    public BookToRead getBookById(Integer id) {
        Optional<BookToRead> book = repository.findById(id);
        return book.orElse(null);
    }

    @Override
    public boolean exists(BookToRead book) {
        return repository.findAll().contains(book);
    }

    public void load(PathDTO input, Language language) {
        List<BookToRead> books = JSONHandler.IO.readJSONFile(input.getPath(), TO_READ, language);

        if (books.size() != 0) {
            clearLanguage(language);
            addAll(books);
        }
    }

    public void loadBatch(PathDTO input, Language language) {
        List<BookToRead> books = JSONHandler.IO.readJSONFile(input.getPath(), TO_READ, language);

        if (books.size() != 0) {
            addAll(books);
        }
    }

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

    public void setUpView(Model model, Language language) {
        List<BookToRead> books = findAllByOrderByIdAsc(language);

        books = sortList(books);

        model.addAttribute("booksToRead", books);
    }

    @Override
    public List<BookToRead> findAll() {
        return repository.findAll();
    }
}
