package qiwi.controllers.russian;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.FinishedBookController;
import qiwi.controllers.enums.Language;
import qiwi.controllers.enums.SortBy;
import qiwi.model.common.Input;
import qiwi.model.russian.AdditionalDatesRussian;
import qiwi.model.russian.FinishedBookRussian;
import qiwi.service.russian.AdditionalDatesServiceRussianImpl;
import qiwi.service.russian.FinishedBookRussianServiceImpl;

import java.util.ArrayList;

@Controller
@RequestMapping("/finishedbooks/russian")
public class FinishedBookRussianController extends FinishedBookController<
        FinishedBookRussian,
        FinishedBookRussianServiceImpl,
        AdditionalDatesRussian,
        AdditionalDatesServiceRussianImpl> {
    @PostMapping("/add")
    public String add(@ModelAttribute("finishedRussianInput") Input inputFinished) {
        super.add(inputFinished, new FinishedBookRussian(), new AdditionalDatesRussian());
        return "redirect:/finishedbooks/russian/";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("finishedRussianInput") Input inputFinished, BindingResult result) {
        super.edit(inputFinished);
        return "redirect:/finishedbooks/russian/";
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
    public String load(@ModelAttribute("finishedRussianInput") Input input) {
        super.load(input, Language.RUSSIAN);
        return "redirect:/finishedbooks/russian/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("finishedRussianInput") Input input) {
        super.save(input, Language.RUSSIAN);
        return "redirect:/finishedbooks/russian/";
    }

    @GetMapping("/")
    public String list(Model model) {
        super.list(model, new ArrayList<>());
        return "finishedBooksRussian";
    }
}