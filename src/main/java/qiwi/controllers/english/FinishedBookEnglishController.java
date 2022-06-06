package qiwi.controllers.english;

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

import static qiwi.util.enums.Language.ENGLISH;

@Controller
@RequestMapping("/finishedbooks/english")
public class FinishedBookEnglishController extends FinishedBookController {
    private final Language language = ENGLISH;

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("book", new FinishedBook());

        return "finished-books/english/add-book";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("book") FinishedBook book, BindingResult result, Model model) {
        validator.validate(book, result);

        if (result.hasErrors()) {
            setUpView(model, language);

            return "finished-books/english/add-book";
        }

        if (service.exists(book)) {
            FinishedBook bookFromLibrary = service.get(book);
            AdditionalDate additionalDate = new AdditionalDate();

            additionalDate.setStart(bookFromLibrary.getStart());
            additionalDate.setEnd(bookFromLibrary.getEnd());

            if (!book.hasDate(additionalDate)) {
                book.addDate(additionalDate);
                service.addBook(book);

                return "redirect:/finishedbooks/english/";
            } else {
                setUpView(model, language);
                result.reject("alreadyExists", "This book already exists.");

                return "finished-books/english/add-book";
            }
        }

        service.addBook(book);

        return "redirect:/finishedbooks/english/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("book", service.getBookById(id));

        return "finished-books/english/edit-book";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("book") FinishedBook book, BindingResult result, Model model) {
        validator.validate(book, result);

        if (result.hasErrors()) {
            setUpView(model, language);

            return "finished-books/english/edit-book";
        }

        if (service.exists(book)) {
            FinishedBook bookFromLibrary = service.get(book);
            AdditionalDate additionalDate = new AdditionalDate();

            additionalDate.setStart(book.getStart());
            additionalDate.setEnd(book.getEnd());

            if (!bookFromLibrary.hasDate(additionalDate)) {
                bookFromLibrary.addDate(additionalDate);
                service.addBook(bookFromLibrary);

                return "redirect:/finishedbooks/english/";
            } else {
                setUpView(model, language);
                result.reject("alreadyExists", "This book already exists.");

                return "finished-books/english/edit-book";
            }
        }

        service.addBook(book);

        return "redirect:/finishedbooks/english/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBookById(id);
        return "redirect:/finishedbooks/english/";
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        super.sort(SortBy.valueOf(property.toUpperCase()));
        return "redirect:/finishedbooks/english/";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("finishedEnglishInput") PathInput input) {
        super.load(input, language);
        return "redirect:/finishedbooks/english/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("finishedEnglishInput") PathInput input) {
        super.save(input, language);
        return "redirect:/finishedbooks/english/";
    }

    @GetMapping("/")
    public String showAllBooks(Model model) {
        setUpView(model, language);
        return "finished-books/" + language.firstLetterToUpperCase() + "/index";
    }
}