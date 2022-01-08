package qiwi.controllers.russian;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.FinishedBookController;
import qiwi.model.input.Input;
import qiwi.model.input.PathInput;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;

import static qiwi.util.enums.Language.RUSSIAN;

@Controller
@RequestMapping("/finishedbooks/russian")
public class FinishedBookRussianController extends FinishedBookController {
    private final Language language = RUSSIAN;

    @PostMapping("/add")
    public String add(@ModelAttribute("finishedRussianInput") Input input, BindingResult result, Model model) {
        return super.add(input, model, language);
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("finishedRussianInput") Input input, BindingResult result, Model model) {
        return super.edit(input, model, language);
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
        return "finishedBooks" + language.firstLetterToUpperCase();
    }
}