package qiwi.controllers.spanish;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.FinishedBookController;
import qiwi.dto.PathDTO;
import qiwi.model.book.FinishedBook;
import qiwi.model.enums.Language;

import static qiwi.model.enums.Language.SPANISH;

@Controller
@RequestMapping("/finishedbooks/spanish")
public class FinishedBookSpanishController extends FinishedBookController {
    private final Language language = SPANISH;

    @GetMapping("/add")
    public String add(Model model) {
        return super.add(model, language);
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("book") FinishedBook book, BindingResult result, Model model) {
        return super.add(book, result, model, language);
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        return super.edit(id, model, language);
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("book") FinishedBook book, BindingResult result, Model model) {
        return super.edit(book, result, model, language);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        return super.delete(id, language);
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        return super.sort(property, language);
    }

    @GetMapping("/load")
    public String load(Model model) {
        return super.load(model, language);
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("path") PathDTO input) {
        return super.load(input, language);
    }

    @GetMapping("/save")
    public String save(Model model) {
        return super.save(model, language);
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("path") PathDTO input) {
        return super.save(input, language);
    }

    @GetMapping("/")
    public String showAllBooks(Model model) {
        return super.showAllBooks(model, language);
    }
}