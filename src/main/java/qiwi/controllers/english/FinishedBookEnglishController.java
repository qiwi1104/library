package qiwi.controllers.english;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.FinishedBookController;
import qiwi.model.AdditionalDates;
import qiwi.model.book.FinishedBook;
import qiwi.model.input.Input;
import qiwi.model.input.PathInput;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;

import static qiwi.util.enums.Language.ENGLISH;

@Controller
@RequestMapping("/finishedbooks/english")
public class FinishedBookEnglishController extends FinishedBookController {
    private final Language language = ENGLISH;

    @PostMapping("/add")
    public String add(@ModelAttribute("finishedEnglishInput") Input input, BindingResult result, Model model) {
        return getRedirectionAddress(input, result, model, language, new FinishedBook(), new AdditionalDates());
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("finishedEnglishInput") Input input, BindingResult result, Model model) {
        return getRedirectionAddress(input, result, model, language, "finishedbooks", service.findAll());
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        super.delete(id, ENGLISH);
        return "redirect:/finishedbooks/english/";
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        super.sort(SortBy.valueOf(property.toUpperCase()));
        return "redirect:/finishedbooks/english/";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("finishedEnglishInput") PathInput input, BindingResult result, Model model) {
        super.load(input, language);
        return "redirect:/finishedbooks/english/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("finishedEnglishInput") PathInput input, BindingResult result, Model model) {
        super.save(input, language);
        return "redirect:/finishedbooks/english/";
    }

    @GetMapping("/")
    public String list(Model model) {
        return showTable(model, language);
    }
}