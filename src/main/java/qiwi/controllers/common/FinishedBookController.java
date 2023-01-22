package qiwi.controllers.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import qiwi.dto.PathDTO;
import qiwi.model.book.FinishedBook;
import qiwi.model.enums.Language;
import qiwi.model.enums.SortBy;
import qiwi.service.service.FinishedBookService;

public abstract class FinishedBookController extends BookController {
    @Autowired
    private FinishedBookService finishedBookService;

    public String add(Model model, Language language) {
        model.addAttribute("book", new FinishedBook());

        return "finished-books/" + language.toLowerCase() + "/add-book";
    }

    public String add(FinishedBook book, BindingResult result, Model model,
                      Language language) {
        return finishedBookService.addBook(book, result, model, language)
                ?"redirect:/finishedbooks/" + language.toLowerCase() + "/"
                : "finished-books/" + language.toLowerCase() + "/add-book";
    }

    public String edit(Integer id, Model model, Language language) {
        model.addAttribute("book", finishedBookService.getBookById(id));

        return "finished-books/" + language.toLowerCase() + "/edit-book";
    }

    public String edit(FinishedBook book, BindingResult result, Model model,
                       Language language) {
        return finishedBookService.editBook(book, result, model, language)
                ? "redirect:/finishedbooks/" + language.toLowerCase() + "/"
                : "finished-books/" + language.toLowerCase() + "/edit-book";
    }

    public String delete(Integer id, Language language) {
        finishedBookService.deleteBookById(id);
        return "redirect:/finishedbooks/" + language.toLowerCase() + "/";
    }

    public String sort(String property, Language language) {
        finishedBookService.sort(SortBy.valueOf(property.toUpperCase()));
        return "redirect:/finishedbooks/" + language.toLowerCase() + "/";
    }

    public String load(Model model, Language language) {
        model.addAttribute("path", new PathDTO());
        return "finished-books/" + language.toLowerCase() + "/load";
    }

    public String load(PathDTO input, Language language) {
        finishedBookService.load(input, language);
        return "redirect:/finishedbooks/" + language.toLowerCase() + "/";
    }

    public String save(Model model, Language language) {
        model.addAttribute("path", new PathDTO());
        return "finished-books/" + language.toLowerCase() + "/save";
    }

    public String save(PathDTO input, Language language) {
        finishedBookService.save(input, language);
        return "redirect:/finishedbooks/" + language.toLowerCase() + "/";
    }

    public String showAllBooks(Model model, Language language) {
        finishedBookService.setUpView(model, language);
        return "finished-books/" + language.toLowerCase() + "/index";
    }
}
