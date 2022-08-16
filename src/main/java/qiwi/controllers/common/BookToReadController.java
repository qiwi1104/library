package qiwi.controllers.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.SessionAttribute;
import qiwi.dto.PathDTO;
import qiwi.model.book.BookToRead;
import qiwi.model.book.FinishedBook;
import qiwi.model.enums.Language;
import qiwi.model.enums.SortBy;
import qiwi.service.dao.BookToReadDAO;
import qiwi.service.service.BookToReadService;

public abstract class BookToReadController extends BookController {
    @Autowired
    private BookToReadService bookToReadService;
    @Autowired
    private BookToReadDAO bookToReadDAO;

    public String add(Model model, Language language) {
        model.addAttribute("book", new BookToRead());

        return "books-to-read/" + language.toLowerCase() + "/add-book";
    }

    public String add(BookToRead book, BindingResult result, Model model,
                      Language language) {
        return bookToReadService.addBook(book, result, model, language)
                ? "redirect:/bookstoread/" + language.toLowerCase() + "/"
                : "books-to-read/" + language.toLowerCase() + "/add-book";
    }

    public String edit(Integer id, Model model, Language language) {
        model.addAttribute("book", bookToReadDAO.getBookById(id));

        return "books-to-read/" + language.toLowerCase() + "/edit-book";
    }

    public String edit(BookToRead book, BindingResult result, Model model,
                       Language language) {
        return bookToReadService.editBook(book, result, model, language)
                ? "redirect:/bookstoread/" + language.toLowerCase() + "/"
                : "books-to-read/" + language.toLowerCase() + "/edit-book";
    }

    public String delete(Integer id, Language language) {
        bookToReadDAO.deleteBookById(id);
        return "redirect:/bookstoread/" + language.toLowerCase() + "/";
    }

    public String finish(Integer id, Model model, Language language) {
        BookToRead book = bookToReadDAO.getBookById(id);

        model.addAttribute("book", new FinishedBook(book));
        model.addAttribute("id", book.getId());

        return "books-to-read/" + language.toLowerCase() + "/finish-book";
    }

    public String finish(FinishedBook book, @SessionAttribute("id") Integer id,
                         Language language) {
        bookToReadService.finishBook(book, id);
        return "redirect:/bookstoread/" + language.toLowerCase() + "/";
    }

    public String sort(String property, Language language) {
        bookToReadService.sort(SortBy.valueOf(property.toUpperCase()));
        return "redirect:/bookstoread/" + language.toLowerCase() + "/";
    }

    public String load(Model model, Language language) {
        model.addAttribute("path", new PathDTO());
        return "books-to-read/" + language.toLowerCase() + "/load";
    }

    public String load(PathDTO input, Language language) {
        bookToReadService.load(input, language);
        return "redirect:/bookstoread/" + language.toLowerCase() + "/";
    }

    public String loadBatch(Model model, Language language) {
        model.addAttribute("path", new PathDTO());
        return "books-to-read/" + language.toLowerCase() + "/load-batch";
    }

    public String loadBatch(PathDTO input, Language language) {
        bookToReadService.loadBatch(input, language);
        return "redirect:/bookstoread/" + language.toLowerCase() + "/";
    }

    public String save(Model model, Language language) {
        model.addAttribute("path", new PathDTO());
        return "books-to-read/" + language.toLowerCase() + "/save";
    }

    public String save(PathDTO input, Language language) {
        bookToReadService.save(input, language);
        return "redirect:/bookstoread/" + language.toLowerCase() + "/";
    }

    public String showAllBooks(Model model, Language language) {
        bookToReadService.setUpView(model, language);
        return "books-to-read/" + language.toLowerCase() + "/index";
    }
}
