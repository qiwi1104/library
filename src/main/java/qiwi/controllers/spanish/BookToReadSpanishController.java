package qiwi.controllers.spanish;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.BookToReadController;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;
import qiwi.model.common.input.Input;
import qiwi.model.common.input.Input;
import qiwi.model.common.input.PathInput;
import qiwi.model.spanish.BookToReadSpanish;
import qiwi.model.spanish.FinishedBookSpanish;
import qiwi.service.spanish.BookToReadSpanishServiceImpl;
import qiwi.service.spanish.FinishedBookSpanishServiceImpl;

import java.util.ArrayList;

@Controller
@RequestMapping("/bookstoread/spanish")
public class BookToReadSpanishController extends BookToReadController<
        BookToReadSpanish,
        BookToReadSpanishServiceImpl,
        FinishedBookSpanish,
        FinishedBookSpanishServiceImpl> {

    private void addAttributesToModel(Model model) {
        model.addAttribute("booksToReadSpanishInput", new Input());
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("booksToReadSpanishInput") Input input, BindingResult result, Model model) {
        if (result.hasErrors()) {
            super.list(model, new Input());
            return "booksToReadSpanish";
        }

        super.add(input, new BookToReadSpanish());
        return "redirect:/bookstoread/spanish/";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("booksToReadSpanishInput") Input input, BindingResult result, Model model) {
        if (result.hasErrors()) {
            if (input.getId() == null) {
                super.list(model, new Input());
                return "booksToReadSpanish";
            }
        }

        super.edit(input);
        return "redirect:/bookstoread/spanish/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBook(id);
        return "redirect:/bookstoread/spanish/";
    }

    @PostMapping("/finish/{id}")
    public String finish(@ModelAttribute("booksToReadSpanishInput") Input input, BindingResult result, Model model) {
        if (result.hasErrors()) {
            super.list(model, input);
            return "booksToReadSpanish";
        }

        super.finish(input, new FinishedBookSpanish());
        return "redirect:/bookstoread/spanish/";
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        super.sort(SortBy.valueOf(property.toUpperCase()));
        return "redirect:/bookstoread/spanish/";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("booksToReadSpanishInput") PathInput input, BindingResult result, Model model) {
        super.load(input, Language.SPANISH);
        return "redirect:/bookstoread/spanish/";
    }

    @PostMapping("/loadBatch")
    public String loadBatch(@ModelAttribute("booksToReadSpanishInput") PathInput input, BindingResult result, Model model) {
        super.loadBatch(input, Language.SPANISH);
        return "redirect:/bookstoread/spanish/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("booksToReadSpanishInput") PathInput input, BindingResult result, Model model) {
        super.save(input, Language.SPANISH);
        return "redirect:/bookstoread/spanish/";
    }

    @GetMapping("/")
    public String list(Model model) {
        addAttributesToModel(model);
        super.list(model, new Input());
        return "booksToReadSpanish";
    }
}
