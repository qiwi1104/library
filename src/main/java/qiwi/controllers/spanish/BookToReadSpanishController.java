package qiwi.controllers.spanish;

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

import static qiwi.util.enums.Language.SPANISH;

@Controller
@RequestMapping("/bookstoread/spanish")
public class BookToReadSpanishController extends BookToReadController {

    @PostMapping("/add")
    public String add(@ModelAttribute("booksToReadSpanishInput") Input input, BindingResult result, Model model) {
        return getRedirectionAddress(input, result, model, SPANISH, new BookToRead());
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("booksToReadSpanishInput") Input input, BindingResult result, Model model) {
        return getRedirectionAddress(input, result, model, SPANISH, "bookstoread");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBook(id);
        return "redirect:/bookstoread/spanish/";
    }

    @PostMapping("/finish/{id}")
    public String finish(@ModelAttribute("booksToReadSpanishInput") Input input, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return showTable(model, SPANISH);
        }

        if (super.finish(input, model, new FinishedBook())) {
            return "redirect:/bookstoread/spanish/";
        } else {
            return showTable(model, SPANISH);
        }
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        super.sort(SortBy.valueOf(property.toUpperCase()));
        return "redirect:/bookstoread/spanish/";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("booksToReadSpanishInput") PathInput input, BindingResult result, Model model) {
        super.load(input, SPANISH);
        return "redirect:/bookstoread/spanish/";
    }

    @PostMapping("/loadBatch")
    public String loadBatch(@ModelAttribute("booksToReadSpanishInput") PathInput input, BindingResult result, Model model) {
        super.loadBatch(input, SPANISH);
        return "redirect:/bookstoread/spanish/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("booksToReadSpanishInput") PathInput input, BindingResult result, Model model) {
        super.save(input, SPANISH);
        return "redirect:/bookstoread/spanish/";
    }

    @GetMapping("/")
    public String list(Model model) {
        return showTable(model, SPANISH);
    }
}