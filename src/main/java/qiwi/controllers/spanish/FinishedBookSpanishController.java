package qiwi.controllers.spanish;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.FinishedBookController;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;
import qiwi.model.common.input.Input;
import qiwi.model.common.input.PathInput;
import qiwi.model.spanish.AdditionalDatesSpanish;
import qiwi.model.spanish.FinishedBookSpanish;
import qiwi.service.spanish.AdditionalDatesSpanishServiceImpl;
import qiwi.service.spanish.FinishedBookSpanishServiceImpl;

import java.util.ArrayList;

@Controller
@RequestMapping("/finishedbooks/spanish")
public class FinishedBookSpanishController extends FinishedBookController<
        FinishedBookSpanish,
        FinishedBookSpanishServiceImpl,
        AdditionalDatesSpanish,
        AdditionalDatesSpanishServiceImpl> {

    private void addAttributesToModel(Model model) {
        model.addAttribute("finishedSpanishInput", new Input());
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("finishedSpanishInput") Input input, BindingResult result, Model model) {
        if (result.hasErrors()) {
            super.list(model, new ArrayList<>());
            return "finishedBooksSpanish";
        }

        super.add(input, new FinishedBookSpanish(), new AdditionalDatesSpanish());
        return "redirect:/finishedbooks/spanish/";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("finishedSpanishInput") Input input, BindingResult result, Model model) {
        if (result.hasErrors()) {
            if (input.getId() == null) {
                super.list(model, new ArrayList<>());
                return "finishedBooksSpanish";
            }
        }

        super.edit(input);
        return "redirect:/finishedbooks/spanish/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBook(id);
        return "redirect:/finishedbooks/spanish/";
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        super.sort(SortBy.valueOf(property.toUpperCase()));
        return "redirect:/finishedbooks/spanish/";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("finishedSpanishInput") PathInput input, BindingResult result, Model model) {
        super.load(input, Language.SPANISH);
        return "redirect:/finishedbooks/spanish/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("finishedSpanishInput") PathInput input, BindingResult result, Model model) {
        super.save(input, Language.SPANISH);
        return "redirect:/finishedbooks/spanish/";
    }

    @GetMapping("/")
    public String list(Model model) {
        addAttributesToModel(model);
        super.list(model, new ArrayList<>());
        return "finishedBooksSpanish";
    }
}