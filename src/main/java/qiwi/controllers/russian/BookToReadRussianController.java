package qiwi.controllers.russian;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.BookToReadController;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;
import qiwi.model.common.input.FinishedBookInput;
import qiwi.model.common.input.Input;
import qiwi.model.common.input.PathInput;
import qiwi.model.russian.BookToReadRussian;
import qiwi.model.russian.FinishedBookRussian;
import qiwi.service.russian.BookToReadRussianServiceImpl;
import qiwi.service.russian.FinishedBookRussianServiceImpl;

import java.util.ArrayList;

@Controller
@RequestMapping("/bookstoread/russian")
public class BookToReadRussianController extends BookToReadController<
        BookToReadRussian,
        BookToReadRussianServiceImpl,
        FinishedBookRussian,
        FinishedBookRussianServiceImpl> {

    private void addAttributesToModel(Model model) {
        model.addAttribute("booksToReadRussianInput", new Input());
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("booksToReadRussianInput") Input input, BindingResult result, Model model) {
        if (result.hasErrors()) {
            super.list(model, new ArrayList<>());
            return "booksToReadRussian";
        }

        super.add(input, new BookToReadRussian());
        return "redirect:/bookstoread/russian/";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("booksToReadRussianInput") Input input, BindingResult result, Model model) {
        if (result.hasErrors()) {
            if (input.getId() == null) {
                super.list(model, new ArrayList<>());
                return "booksToReadRussian";
            }
        }

        super.edit(input);
        return "redirect:/bookstoread/russian/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBook(id);
        return "redirect:/bookstoread/russian/";
    }

    @PostMapping("/finish/{id}")
    public String finish(@ModelAttribute("booksToReadRussianInput") FinishedBookInput input, BindingResult result, Model model) {
        if (result.hasErrors()) {
            super.list(model, new ArrayList<>());
            return "booksToReadEnglish";
        }

        super.finish(input, new FinishedBookRussian());
        return "redirect:/bookstoread/russian/";
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        super.sort(SortBy.valueOf(property.toUpperCase()));
        return "redirect:/bookstoread/russian/";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("booksToReadRussianInput") PathInput input, BindingResult result, Model model) {
        super.load(input, Language.RUSSIAN);
        return "redirect:/bookstoread/russian/";
    }

    @PostMapping("/loadBatch")
    public String loadBatch(@ModelAttribute("booksToReadRussianInput") PathInput input, BindingResult result, Model model) {
        super.loadBatch(input, Language.RUSSIAN);
        return "redirect:/bookstoread/russian/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("booksToReadRussianInput") PathInput input, BindingResult result, Model model) {
        super.save(input, Language.RUSSIAN);
        return "redirect:/bookstoread/russian/";
    }

    @GetMapping("/")
    public String list(Model model) {
        addAttributesToModel(model);
        super.list(model, new ArrayList<>());
        return "booksToReadRussian";
    }
}
