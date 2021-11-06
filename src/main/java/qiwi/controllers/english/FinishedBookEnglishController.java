package qiwi.controllers.english;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.FinishedBookController;
import qiwi.model.common.input.Input;
import qiwi.model.common.input.PathInput;
import qiwi.model.english.AdditionalDatesEnglish;
import qiwi.model.english.FinishedBookEnglish;
import qiwi.service.english.AdditionalDatesEnglishServiceImpl;
import qiwi.service.english.FinishedBookEnglishServiceImpl;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;

@Controller
@RequestMapping("/finishedbooks/english")
public class FinishedBookEnglishController extends FinishedBookController<
        FinishedBookEnglish,
        FinishedBookEnglishServiceImpl,
        AdditionalDatesEnglish,
        AdditionalDatesEnglishServiceImpl> {

    @PostMapping("/add")
    public String add(@ModelAttribute("finishedEnglishInput") Input input, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return showTable(input, model, "English");
        }

        if (super.add(input, model, new FinishedBookEnglish(), new AdditionalDatesEnglish()))
            return "redirect:/finishedbooks/english/";
        else return showTable(input, model, "English");
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("finishedEnglishInput") Input input, BindingResult result, Model model) {
        if (result.hasErrors()) {
            if (input.getId() == null) {
                return showTable(input, model, "English");
            } else {
                if (super.edit(input, model))
                    return "redirect:/finishedbooks/english/";
                else return showTable(input, model, "English");
            }
        }

        if (super.edit(input, model))
            return "redirect:/finishedbooks/english/";
        else return showTable(input, model, "English");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBook(id);
        return "redirect:/finishedbooks/english/";
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        super.sort(SortBy.valueOf(property.toUpperCase()));
        return "redirect:/finishedbooks/english/";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("finishedEnglishInput") PathInput input, BindingResult result, Model model) {
        super.load(input, Language.ENGLISH);
        return "redirect:/finishedbooks/english/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("finishedEnglishInput") PathInput input, BindingResult result, Model model) {
        super.save(input, Language.ENGLISH);
        return "redirect:/finishedbooks/english/";
    }

    @GetMapping("/")
    public String list(Model model) {
        return showTable(new Input(), model, "English");
    }
}