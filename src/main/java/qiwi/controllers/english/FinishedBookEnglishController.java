package qiwi.controllers.english;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.FinishedBookController;
import qiwi.controllers.enums.Language;
import qiwi.controllers.enums.SortBy;
import qiwi.model.common.input.FinishedBookInput;
import qiwi.model.common.input.Input;
import qiwi.model.common.input.PathInput;
import qiwi.model.english.AdditionalDatesEnglish;
import qiwi.model.english.FinishedBookEnglish;
import qiwi.service.english.AdditionalDatesEnglishServiceImpl;
import qiwi.service.english.FinishedBookEnglishServiceImpl;

import java.util.ArrayList;

@Controller
@RequestMapping("/finishedbooks/english")
public class FinishedBookEnglishController extends FinishedBookController<
        FinishedBookEnglish,
        FinishedBookEnglishServiceImpl,
        AdditionalDatesEnglish,
        AdditionalDatesEnglishServiceImpl> {

    private void addAttributesToModel(Model model) {
        model.addAttribute("finishedEnglishInput", new Input());
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("finishedEnglishInput") FinishedBookInput input, BindingResult result, Model model) {
        if (result.hasErrors()) {
            super.list(model, new ArrayList<>());
            return "finishedBooksEnglish";
        }

        super.add(input, new FinishedBookEnglish(), new AdditionalDatesEnglish());
        return "redirect:/finishedbooks/english/";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("finishedEnglishInput") FinishedBookInput input, BindingResult result, Model model) {
        if (result.hasErrors()) {
            if (input.getId() == null) {
                super.list(model, new ArrayList<>());
                return "finishedBooksEnglish";
            }
        }

        super.edit(input);
        return "redirect:/finishedbooks/english/";
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
        addAttributesToModel(model);
        super.list(model, new ArrayList<>());
        return "finishedBooksEnglish";
    }
}