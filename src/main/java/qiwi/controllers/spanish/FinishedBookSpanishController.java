package qiwi.controllers.spanish;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.FinishedBookController;
import qiwi.controllers.enums.Language;
import qiwi.controllers.enums.SortBy;
import qiwi.model.common.Input;
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
    @PostMapping("/add")
    public String add(@ModelAttribute("finishedSpanishInput") Input inputFinished) {
        super.add(inputFinished, new FinishedBookSpanish(), new AdditionalDatesSpanish());
        return "redirect:/finishedbooks/spanish/";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("finishedSpanishInput") Input inputFinished, BindingResult result) {
        super.edit(inputFinished);
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
    public String load(@ModelAttribute("finishedSpanishInput") Input input) {
        super.load(input, Language.SPANISH);
        return "redirect:/finishedbooks/spanish/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("finishedSpanishInput") Input input) {
        super.save(input, Language.SPANISH);
        return "redirect:/finishedbooks/spanish/";
    }

    @GetMapping("/")
    public String list(Model model) {
        super.list(model, new ArrayList<>());
        return "finishedBooksSpanish";
    }
}