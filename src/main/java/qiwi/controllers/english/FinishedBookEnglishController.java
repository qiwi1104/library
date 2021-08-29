package qiwi.controllers.english;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.FinishedBookController;
import qiwi.controllers.enums.Language;
import qiwi.controllers.enums.SortBy;
import qiwi.model.common.Input;
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

    @PostMapping("/add")
    public String add(@ModelAttribute("finishedEnglishInput") Input inputFinished) {
        super.add(inputFinished, new FinishedBookEnglish(), new AdditionalDatesEnglish());
        return "redirect:/finishedbooks/english/";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("finishedEnglishInput") Input inputFinished, BindingResult result) {
        super.edit(inputFinished, service.getBookById(inputFinished.getId()));
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
    public String load(@ModelAttribute("finishedEnglishInput") Input input) {
        super.load(input, Language.ENGLISH);
        return "redirect:/finishedbooks/english/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("finishedEnglishInput") Input input) {
        super.save(input, Language.ENGLISH);
        return "redirect:/finishedbooks/english/";
    }

    @GetMapping("/")
    public String list(Model model) {
        super.list(model, new ArrayList<FinishedBookEnglish>());
        return "finishedBooksEnglish";
    }
}