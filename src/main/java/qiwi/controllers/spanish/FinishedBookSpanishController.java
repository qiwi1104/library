package qiwi.controllers.spanish;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.FinishedBookController;
import qiwi.model.input.Input;
import qiwi.model.input.PathInput;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;

import static qiwi.util.enums.Language.SPANISH;

@Controller
@RequestMapping("/finishedbooks/spanish")
public class FinishedBookSpanishController extends FinishedBookController {
    private final Language language = SPANISH;

    @PostMapping("/add")
    public String add(@ModelAttribute("finishedSpanishInput") Input input, BindingResult result, Model model) {
        return getRedirectionAddress(input, result, model, language);
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("finishedSpanishInput") Input input, BindingResult result, Model model) {
        return getRedirectionAddress(input, result, model, language, "finishedbooks");
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
    public String list(Model model) {
        return showTable(model, language);
    }
}
