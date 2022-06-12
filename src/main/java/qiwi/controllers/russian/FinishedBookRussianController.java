package qiwi.controllers.russian;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.FinishedBookController;
import qiwi.input.PathInput;
import qiwi.model.AdditionalDate;
import qiwi.model.book.FinishedBook;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;

import static qiwi.util.enums.Language.RUSSIAN;

@Controller
@RequestMapping("/finishedbooks/russian")
public class FinishedBookRussianController extends FinishedBookController {
    private final Language language = RUSSIAN;

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("book", new FinishedBook());

        return "finished-books/russian/add-book";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("book") FinishedBook book, BindingResult result, Model model) {
        validator.validate(book, result);

        if (result.hasErrors()) {
            setUpView(model, language);

            return "finished-books/russian/add-book";
        }

        if (service.exists(book)) {
            FinishedBook bookFromLibrary = service.get(book);
            AdditionalDate additionalDate = new AdditionalDate();

            additionalDate.setStart(bookFromLibrary.getStart());
            additionalDate.setEnd(bookFromLibrary.getEnd());

            if (!book.hasDate(additionalDate)) {
                book.addDate(additionalDate);
                service.addBook(book);

                return "redirect:/finishedbooks/russian/";
            } else {
                setUpView(model, language);
                result.reject("alreadyExists", "This book already exists.");

                return "finished-books/russian/add-book";
            }
        }

        service.addBook(book);

        return "redirect:/finishedbooks/russian/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("book", service.getBookById(id));

        return "finished-books/russian/edit-book";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("book") FinishedBook book, BindingResult result, Model model) {
        validator.validate(book, result);

        if (result.hasErrors()) {
            setUpView(model, language);

            return "finished-books/russian/edit-book";
        }

        if (service.exists(book)) {
            FinishedBook bookFromLibrary = service.get(book);
            AdditionalDate additionalDate = new AdditionalDate();

            additionalDate.setStart(book.getStart());
            additionalDate.setEnd(book.getEnd());

            if (!bookFromLibrary.hasDate(additionalDate)) {
                bookFromLibrary.addDate(additionalDate);
                service.addBook(bookFromLibrary);

                return "redirect:/finishedbooks/russian/";
            } else {
                setUpView(model, language);
                result.reject("alreadyExists", "This book already exists.");

                return "finished-books/russian/edit-book";
            }
        }

        service.addBook(book);

        return "redirect:/finishedbooks/russian/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBookById(id);
        return "redirect:/finishedbooks/russian/";
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        super.sort(SortBy.valueOf(property.toUpperCase()));
        return "redirect:/finishedbooks/russian/";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("finishedRussianInput") PathInput input) {
        super.load(input, language);
        return "redirect:/finishedbooks/russian/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("finishedRussianInput") PathInput input) {
        super.save(input, language);
        return "redirect:/finishedbooks/russian/";
    }

    @GetMapping("/")
    public String showAllBooks(Model model) {
        setUpView(model, language);
        return "finished-books/" + language.firstLetterToUpperCase() + "/index";
    }
}