package qiwi.controllers.spanish;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.FinishedBookController;
import qiwi.model.AdditionalDate;
import qiwi.model.book.FinishedBook;
import qiwi.model.input.PathInput;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;

import static qiwi.util.enums.Language.SPANISH;

@Controller
@RequestMapping("/finishedbooks/spanish")
public class FinishedBookSpanishController extends FinishedBookController {
    private final Language language = SPANISH;

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("book", new FinishedBook());

        return "finished-books/spanish/add-book";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("book") FinishedBook book, BindingResult result, Model model) {
        validator.validate(book, result);

        if (result.hasErrors()) {
            setUpView(model, language);

            return "finished-books/spanish/add-book";
        }

        if (service.exists(book)) {
            FinishedBook bookFromLibrary = service.get(book);
            AdditionalDate additionalDate = new AdditionalDate();

            additionalDate.setStart(bookFromLibrary.getStart());
            additionalDate.setEnd(bookFromLibrary.getEnd());

            if (!book.hasDate(additionalDate)) {
                book.addDate(additionalDate);
                service.addBook(book);

                return "redirect:/finishedbooks/spanish/";
            } else {
                setUpView(model, language);
                result.reject("alreadyExists", "This book already exists.");

                return "finished-books/spanish/add-book";
            }
        }

        service.addBook(book);

        return "redirect:/finishedbooks/spanish/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("book", service.getBookById(id));

        return "finished-books/spanish/edit-book";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("book") FinishedBook book, BindingResult result, Model model) {
        validator.validate(book, result);

        if (result.hasErrors()) {
            setUpView(model, language);

            return "finished-books/spanish/edit-book";
        }

        if (service.exists(book)) {
            FinishedBook bookFromLibrary = service.get(book);
            AdditionalDate additionalDate = new AdditionalDate();

            additionalDate.setStart(book.getStart());
            additionalDate.setEnd(book.getEnd());

            if (!bookFromLibrary.hasDate(additionalDate)) {
                bookFromLibrary.addDate(additionalDate);
                service.addBook(bookFromLibrary);

                return "redirect:/finishedbooks/spanish/";
            } else {
                setUpView(model, language);
                result.reject("alreadyExists", "This book already exists.");

                return "finished-books/spanish/edit-book";
            }
        }

        service.addBook(book);

        return "redirect:/finishedbooks/spanish/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBookById(id);
        return "redirect:/finishedbooks/spanish/";
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        super.sort(SortBy.valueOf(property.toUpperCase()));
        return "redirect:/finishedbooks/spanish/";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("finishedSpanishInput") PathInput input) {
        super.load(input, language);
        return "redirect:/finishedbooks/spanish/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("finishedSpanishInput") PathInput input) {
        super.save(input, language);
        return "redirect:/finishedbooks/spanish/";
    }

    @GetMapping("/")
    public String showAllBooks(Model model) {
        setUpView(model, language);
        return "finished-books/" + language.firstLetterToUpperCase() + "/index";
    }
}