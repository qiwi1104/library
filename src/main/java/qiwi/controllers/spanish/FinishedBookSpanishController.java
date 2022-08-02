package qiwi.controllers.spanish;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.FinishedBookController;
import qiwi.input.PathInput;
import qiwi.model.book.FinishedBook;
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
        return super.add(book, result, model, language);
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("book", service.getBookById(id));

        return "finished-books/spanish/edit-book";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("book") FinishedBook book, BindingResult result, Model model) {
        return super.edit(book, result, model, language);
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

    @GetMapping("/load")
    public String load(Model model) {
        model.addAttribute("path", new PathInput());
        return "finished-books/spanish/load";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("path") PathInput input) {
        super.load(input, language);
        return "redirect:/finishedbooks/spanish/";
    }

    @GetMapping("/save")
    public String save(Model model) {
        model.addAttribute("path", new PathInput());
        return "finished-books/spanish/save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("path") PathInput input) {
        super.save(input, language);
        return "redirect:/finishedbooks/spanish/";
    }

    @GetMapping("/")
    public String showAllBooks(Model model) {
        setUpView(model, language);
        return "finished-books/" + language.toLowerCase() + "/index";
    }
}