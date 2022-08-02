package qiwi.controllers.russian;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.FinishedBookController;
import qiwi.dto.PathDTO;
import qiwi.model.book.FinishedBook;
import qiwi.model.enums.Language;
import qiwi.model.enums.SortBy;

import static qiwi.model.enums.Language.RUSSIAN;

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
        return service.addBook(book, result, model, language);
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("book", service.getBookById(id));

        return "finished-books/russian/edit-book";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("book") FinishedBook book, BindingResult result, Model model) {
        return service.editBook(book, result, model, language);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBookById(id);
        return "redirect:/finishedbooks/russian/";
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        service.sort(SortBy.valueOf(property.toUpperCase()));
        return "redirect:/finishedbooks/russian/";
    }

    @GetMapping("/load")
    public String load(Model model) {
        model.addAttribute("path", new PathDTO());
        return "finished-books/russian/load";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("path") PathDTO input) {
        service.load(input, language);
        return "redirect:/finishedbooks/russian/";
    }

    @GetMapping("/save")
    public String save(Model model) {
        model.addAttribute("path", new PathDTO());
        return "finished-books/russian/save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("path") PathDTO input) {
        service.save(input, language);
        return "redirect:/finishedbooks/russian/";
    }

    @GetMapping("/")
    public String showAllBooks(Model model) {
        service.setUpView(model, language);
        return "finished-books/" + language.firstLetterToUpperCase() + "/index";
    }
}