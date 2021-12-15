package qiwi.controllers.russian;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.BookToReadController;
import qiwi.model.input.Input;
import qiwi.model.input.PathInput;
import qiwi.util.enums.SortBy;

import static qiwi.util.enums.Action.ADD;
import static qiwi.util.enums.Action.EDIT;
import static qiwi.util.enums.Language.RUSSIAN;

@Controller
@RequestMapping("/bookstoread/russian")
public class BookToReadRussianController extends BookToReadController {

    @PostMapping("/add")
    public String add(@ModelAttribute("booksToReadRussianInput") Input input, BindingResult result, Model model) {
        return getRedirectionAddress(input, model, RUSSIAN, ADD);
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("booksToReadRussianInput") Input input, BindingResult result, Model model) {
        return getRedirectionAddress(input, model, RUSSIAN, EDIT);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBookById(id);
        return "redirect:/bookstoread/russian/";
    }

    @PostMapping("/finish/{id}")
    public String finish(@ModelAttribute("booksToReadRussianInput") Input input, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return showTable(model, RUSSIAN);
        }

        if (super.finish(input, model)) {
            return "redirect:/bookstoread/russian/";
        } else {
            return showTable(model, RUSSIAN);
        }
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        super.sort(SortBy.valueOf(property.toUpperCase()));
        return "redirect:/bookstoread/russian/";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("booksToReadRussianInput") PathInput input) {
        super.load(input, RUSSIAN);
        return "redirect:/bookstoread/russian/";
    }

    @PostMapping("/loadBatch")
    public String loadBatch(@ModelAttribute("booksToReadRussianInput") PathInput input) {
        super.loadBatch(input, RUSSIAN);
        return "redirect:/bookstoread/russian/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("booksToReadRussianInput") PathInput input) {
        super.save(input, RUSSIAN);
        return "redirect:/bookstoread/russian/";
    }

    @GetMapping("/")
    public String list(Model model) {
        return showTable(model, RUSSIAN);
    }
}