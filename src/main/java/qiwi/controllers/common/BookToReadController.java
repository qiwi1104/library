package qiwi.controllers.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import qiwi.model.common.book.BookToRead;
import qiwi.model.common.book.FinishedBook;
import qiwi.model.common.input.Input;
import qiwi.model.common.input.PathInput;
import qiwi.repository.common.BookToReadRepository;
import qiwi.repository.common.FinishedBookRepository;
import qiwi.service.common.BookToReadServiceImpl;
import qiwi.service.common.FinishedBookServiceImpl;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;
import qiwi.util.enums.SortType;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static qiwi.util.enums.BookType.TO_READ;
import static qiwi.util.enums.Context.ADD;
import static qiwi.util.enums.Context.EDIT;
import static qiwi.util.enums.SortBy.FOUND;
import static qiwi.util.enums.SortType.ASC;
import static qiwi.util.enums.SortType.DESC;

public abstract class BookToReadController<
        T extends BookToRead,
        S extends BookToReadServiceImpl<T, ? extends BookToReadRepository<T>>,
        U extends FinishedBook,
        V extends FinishedBookServiceImpl<U, ? extends FinishedBookRepository<U>>> extends BookController {
    @Autowired
    protected S service;
    @Autowired
    protected V finishedBookService;

    protected SortType sortDateMethod = ASC;
    protected SortBy sortProperty = FOUND;

    private void setIds(List<T> books) {
        int i = service.findAll().size() + 1;
        for (T book : books) {
            book.setId(i);
            i++;
        }
    }

    @Override
    protected List<T> filterAndSort() {
        List<T> books = null;

        switch (sortDateMethod) {
            case ASC:
                switch (sortProperty) {
                    case ID:
                        books = service.findAllByOrderByIdAsc();
                        break;
                    case FOUND:
                        books = service.findAllByOrderByFoundByIdAsc();
                        break;
                }
                break;
            case DESC:
                switch (sortProperty) {
                    case ID:
                        books = service.findAllByOrderByIdDesc();
                        break;
                    case FOUND:
                        books = service.findAllByOrderByFoundByIdDesc();
                        break;
                }
                break;
        }
        return books;
    }

    protected boolean add(Input input, Model model, T book) {
        book.setId(service.findAll().size() + 1);
        setBookAttributesFromInput(book, input, ADD);

        if (!service.exists(book)) {
            service.addBook(book);
            return true;
        } else {
            model.addAttribute("alreadyExistsMessage", "");
            return false;
        }
    }

    protected void edit(Input input) {
        T book = service.getBookById(input.getId());
        setBookAttributesFromInput(book, input, EDIT);
        service.addBook(book);
    }

    protected void finish(Input input, U finishedBook) {
        T bookToRead = service.getBookById(input.getId());

        finishedBook.setId(finishedBookService.findAll().size() + 1);
        finishedBook.setAuthor(bookToRead.getAuthor());
        finishedBook.setName(bookToRead.getName());
        finishedBook.setStart(input.getStart());
        finishedBook.setEnd(input.getEnd());
        finishedBook.setFound(bookToRead.getFound());
        finishedBook.setDescription(bookToRead.getDescription());

        finishedBookService.addBook(finishedBook);
        service.deleteBook(bookToRead.getId());
    }

    protected boolean finish(Input input, Model model, U finishedBook) {
        T bookToRead = service.getBookById(input.getId());

        if (bookToRead != null) {
            finishedBook.setId(finishedBookService.findAll().size() + 1);
            finishedBook.setAuthor(bookToRead.getAuthor());
            finishedBook.setName(bookToRead.getName());
            finishedBook.setStart(input.getStart());
            finishedBook.setEnd(input.getEnd());
            finishedBook.setFound(bookToRead.getFound());
            finishedBook.setDescription(bookToRead.getDescription());

            finishedBookService.addBook(finishedBook);
            service.deleteBook(bookToRead.getId());
            return true;
        } else {
            model.addAttribute("nonExistentMessage", "");
            return false;
        }
    }

    protected void sort(SortBy sortProperty) {
        sortDateMethod = sortDateMethod.equals(ASC) ? DESC : ASC;
        this.sortProperty = sortProperty;
    }

    protected void load(PathInput input, Language language) {
        List<T> books = JSONHandler.IO.readJSONFile(input.getPath(), TO_READ, language);

        if (books.size() != 0) {
            service.clearAll();
            service.addAll(books);
        }
    }

    protected void loadBatch(PathInput input, Language language) {
        List<T> books = JSONHandler.IO.readJSONFile(input.getPath(), TO_READ, language);

        if (books.size() != 0) {
            setIds(books);
            service.addAll(books);
        } else {
            System.out.println("The list is empty :(");
        }
    }

    protected void save(PathInput input, Language language) {
        List<T> bookToReadList = service.findAll();
        JSONHandler.IO.saveTableToJSON(bookToReadList, input.getPath(), language, TO_READ);
    }

    protected void list(Model model, String language, Input input) {
        List<T> bookList = filterAndSort();

        model.addAttribute("booksToRead" + language + "Input", input);
        model.addAttribute("booksToRead", bookList);
    }
}
