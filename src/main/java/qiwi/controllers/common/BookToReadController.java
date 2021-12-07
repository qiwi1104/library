package qiwi.controllers.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import qiwi.model.book.BookToRead;
import qiwi.model.book.FinishedBook;
import qiwi.model.input.Input;
import qiwi.model.input.PathInput;
import qiwi.service.impl.BookToReadServiceImpl;
import qiwi.service.impl.FinishedBookServiceImpl;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;
import qiwi.util.enums.SortType;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static qiwi.util.enums.BookType.TO_READ;
import static qiwi.util.enums.Context.ADD;
import static qiwi.util.enums.Context.EDIT;
import static qiwi.util.enums.SortBy.FOUND;
import static qiwi.util.enums.SortType.ASC;
import static qiwi.util.enums.SortType.DESC;

public abstract class BookToReadController extends BookController {
    @Autowired
    protected BookToReadServiceImpl service;
    @Autowired
    protected FinishedBookServiceImpl finishedBookService;

    protected SortType sortDateMethod = ASC;
    protected SortBy sortProperty = FOUND;

    /*
     * Returns either a redirection link to the respective page (if there are no errors)
     * Or a view name (if there are errors)
     * */
    protected String getRedirectionAddress(Input input, BindingResult result, Model model, Language language, BookToRead book) {
        if (result.hasErrors())
            return showTable(model, language);

        if (add(input, model, book, language))
            return "redirect:/bookstoread/" + language.toLowerCase() + "/";
        else return showTable(model, language);
    }

    protected List<BookToRead> sortList(List<BookToRead> list) {
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

    protected boolean add(Input input, Model model, BookToRead book, Language language) {
        setBookAttributesFromInput(book, input, ADD, language);

        if (!service.exists(book)) {
            service.addBook(book);
            return true;
        } else {
            model.addAttribute("alreadyExistsMessage", "");
            return false;
        }
    }

    @Override
    protected boolean edit(Input input, Model model, Language language) {
        BookToRead book = service.getBookById(input.getId());

        if (book != null) {
            setBookAttributesFromInput(book, input, EDIT, language);
            service.addBook(book);
            return true;
        } else {
            model.addAttribute("nonExistentMessageEdit", "");
            return false;
        }
    }

    protected boolean finish(Input input, Model model, FinishedBook finishedBook) {
        BookToRead bookToRead = service.getBookById(input.getId());

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
        List<BookToRead> books = JSONHandler.IO.readJSONFile(input.getPath(), TO_READ, language);

        if (books.size() != 0) {
            service.clearLanguage(language);
            service.addAll(books);
        }
    }

    protected void loadBatch(PathInput input, Language language) {
        List<BookToRead> books = JSONHandler.IO.readJSONFile(input.getPath(), TO_READ, language);

        if (books.size() != 0) {
            service.addAll(books);
        } else {
            System.out.println("The list is empty :(");
        }
    }

    protected void save(PathInput input, Language language) {
        List<BookToRead> bookToReadList = service.findAllByOrderByIdAsc(language);
        JSONHandler.IO.saveTableToJSON(bookToReadList, input.getPath(), language, TO_READ);
    }

    @Override
    protected String showTable(Model model, Language language) {
        List<BookToRead> bookList = service.findAllByOrderByIdAsc(language);

        bookList = sortList(bookList);

        model.addAttribute("booksToRead" + language.firstLetterToUpperCase() + "Input", new Input());
        model.addAttribute("booksToRead", bookList);

        return "booksToRead" + language.firstLetterToUpperCase();
    }
}
