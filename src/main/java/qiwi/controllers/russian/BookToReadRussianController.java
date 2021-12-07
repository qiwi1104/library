package qiwi.controllers.russian;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.BookToReadController;
import qiwi.model.book.BookToRead;
import qiwi.model.book.FinishedBook;
import qiwi.model.input.Input;
import qiwi.model.input.PathInput;
import qiwi.util.enums.SortBy;

import static qiwi.util.enums.Language.RUSSIAN;

@Controller
@RequestMapping("/bookstoread/russian")
public class BookToReadRussianController extends BookToReadController {

    @PostMapping("/add")
    public String add(@ModelAttribute("booksToReadRussianInput") Input input, BindingResult result, Model model) {
        return getRedirectionAddress(input, result, model, RUSSIAN, new BookToRead());
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("booksToReadRussianInput") Input input, BindingResult result, Model model) {
        return getRedirectionAddress(input, result, model, RUSSIAN, "bookstoread");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBook(id);
        return "redirect:/bookstoread/russian/";
    }

    @PostMapping("/finish/{id}")
    public String finish(@ModelAttribute("booksToReadRussianInput") Input input, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return showTable(model, RUSSIAN);
        }

        if (super.finish(input, model, new FinishedBook())) {
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
    public String load(@ModelAttribute("booksToReadRussianInput") PathInput input, BindingResult result, Model model) {
        super.load(input, RUSSIAN);
        return "redirect:/bookstoread/russian/";
    }

    @PostMapping("/loadBatch")
    public String loadBatch(@ModelAttribute("booksToReadRussianInput") PathInput input, BindingResult result, Model model) {
        super.loadBatch(input, RUSSIAN);
        return "redirect:/bookstoread/russian/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("booksToReadRussianInput") PathInput input, BindingResult result, Model model) {
        super.save(input, RUSSIAN);
        return "redirect:/bookstoread/russian/";
    }

    @GetMapping("/")
    public String list(Model model) {
        return showTable(model, RUSSIAN);
    }
}