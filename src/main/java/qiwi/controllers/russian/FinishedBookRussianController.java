package qiwi.controllers.russian;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.FinishedBookController;
import qiwi.model.common.input.Input;
import qiwi.model.common.input.PathInput;
import qiwi.model.russian.AdditionalDatesRussian;
import qiwi.model.russian.FinishedBookRussian;
import qiwi.service.russian.AdditionalDatesServiceRussianImpl;
import qiwi.service.russian.FinishedBookRussianServiceImpl;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;

@Controller
@RequestMapping("/finishedbooks/russian")
public class FinishedBookRussianController extends FinishedBookController<
        FinishedBookRussian,
        FinishedBookRussianServiceImpl,
        AdditionalDatesRussian,
        AdditionalDatesServiceRussianImpl> {

    @PostMapping("/add")
    public String add(@ModelAttribute("finishedRussianInput") Input input, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return showTable(input, model, "Russian");
        }

        if (super.add(input, model, new FinishedBookRussian(), new AdditionalDatesRussian()))
            return "redirect:/finishedbooks/russian/";
        else return showTable(input, model, "Russian");
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("finishedRussianInput") Input input, BindingResult result, Model model) {
        if (result.hasErrors()) {
            if (input.getId() == null) {
                return showTable(input, model, "Russian");
            } else {
                if (super.edit(input, model))
                    return "redirect:/finishedbooks/russian/";
                else return showTable(input, model, "Russian");
            }
        }

        if (super.edit(input, model))
            return "redirect:/finishedbooks/russian/";
        else return showTable(input, model, "Russian");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBook(id);
        return "redirect:/finishedbooks/russian/";
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        super.sort(SortBy.valueOf(property.toUpperCase()));
        return "redirect:/finishedbooks/russian/";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("finishedRussianInput") PathInput input, BindingResult result, Model model) {
        super.load(input, Language.RUSSIAN);
        return "redirect:/finishedbooks/russian/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("finishedRussianInput") PathInput input, BindingResult result, Model model) {
        super.save(input, Language.RUSSIAN);
        return "redirect:/finishedbooks/russian/";
    }

    @GetMapping("/")
    public String list(Model model) {
        return showTable(new Input(), model, "Russian");
    }
}