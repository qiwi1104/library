package qiwi.controllers.russian;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.BookToReadController;
import qiwi.controllers.enums.Language;
import qiwi.controllers.enums.SortBy;
import qiwi.model.common.Input;
import qiwi.model.russian.BookToReadRussian;
import qiwi.model.russian.FinishedBookRussian;
import qiwi.service.russian.BookToReadServiceRussianImpl;
import qiwi.service.russian.FinishedBookServiceRussianImpl;

import java.util.ArrayList;

@Controller
@RequestMapping("/bookstoread/russian")
public class BookToReadRussianController extends BookToReadController<
        BookToReadRussian,
        BookToReadServiceRussianImpl,
        FinishedBookRussian,
        FinishedBookServiceRussianImpl> {

    @PostMapping("/add")
    public String add(@ModelAttribute("booksToReadRussianInput") Input input) {
        super.add(input, new BookToReadRussian());
        return "redirect:/bookstoread/russian/";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("booksToReadRussianInput") Input input, BindingResult result) {
        super.edit(input, service.getBookById(input.getId()));
        return "redirect:/bookstoread/russian/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBook(id);
        return "redirect:/bookstoread/russian/";
    }

    @PostMapping("/finish/{id}")
    public String finish(@ModelAttribute("booksToReadRussianInput") Input input) {
        super.finish(input, new FinishedBookRussian());
        return "redirect:/bookstoread/russian/";
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        super.sort(SortBy.valueOf(property.toUpperCase()));
        return "redirect:/bookstoread/russian/";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("booksToReadRussianInput") Input input) {
        super.load(input, Language.RUSSIAN);
        return "redirect:/bookstoread/russian/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("booksToReadRussianInput") Input input) {
        super.save(input, Language.RUSSIAN);
        return "redirect:/bookstoread/russian/";
    }

    @GetMapping("/")
    public String list(Model model) {
        super.list(model, new ArrayList<BookToReadRussian>());
        return "booksToReadRussian";
    }
}
