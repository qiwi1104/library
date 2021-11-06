package qiwi.controllers.russian;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.BookToReadController;
import qiwi.model.common.input.Input;
import qiwi.model.common.input.PathInput;
import qiwi.model.russian.BookToReadRussian;
import qiwi.model.russian.FinishedBookRussian;
import qiwi.service.russian.BookToReadRussianServiceImpl;
import qiwi.service.russian.FinishedBookRussianServiceImpl;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;

@Controller
@RequestMapping("/bookstoread/russian")
public class BookToReadRussianController extends BookToReadController<
        BookToReadRussian,
        BookToReadRussianServiceImpl,
        FinishedBookRussian,
        FinishedBookRussianServiceImpl> {

    @PostMapping("/add")
    public String add(@ModelAttribute("booksToReadRussianInput") Input input, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return showTable(input, model, "Russian");
        }

        if (super.add(input, model, new BookToReadRussian())) {
            return "redirect:/bookstoread/russian/";
        } else {
            return showTable(input, model, "Russian");
        }
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("booksToReadRussianInput") Input input, BindingResult result, Model model) {
        if (result.hasErrors()) {
            if (input.getId() == null) {
                return showTable(input, model, "Russian");
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
    public String finish(@ModelAttribute("booksToReadRussianInput") Input input, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return showTable(input, model, "Russian");
        }

        if (super.finish(input, model, new FinishedBookRussian())) {
            return "redirect:/bookstoread/russian/";
        } else {
            return showTable(input, model, "Russian");
        }
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
        return showTable(new Input(), model, "Russian");
    }
}
