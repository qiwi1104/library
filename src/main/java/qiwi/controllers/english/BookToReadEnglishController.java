package qiwi.controllers.english;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.BookToReadController;
import qiwi.controllers.enums.Language;
import qiwi.controllers.enums.SortBy;
import qiwi.model.common.Input;
import qiwi.model.english.BookToReadEnglish;
import qiwi.model.english.FinishedBookEnglish;
import qiwi.service.english.BookToReadEnglishServiceImpl;
import qiwi.service.english.FinishedBookEnglishServiceImpl;

import java.util.ArrayList;

@Controller
@RequestMapping("/bookstoread/english")
public class BookToReadEnglishController extends BookToReadController<
        BookToReadEnglish,
        BookToReadEnglishServiceImpl,
        FinishedBookEnglish,
        FinishedBookEnglishServiceImpl> {

    @PostMapping("/add")
    public String add(@ModelAttribute("booksToReadEnglishInput") Input input) {
        super.add(input, new BookToReadEnglish());
        return "redirect:/bookstoread/english/";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("booksToReadEnglishInput") Input input, BindingResult result) {
        super.edit(input, service.getBookById(input.getId()));
        return "redirect:/bookstoread/english/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBook(id);
        return "redirect:/bookstoread/english/";
    }

    @PostMapping("/finish/{id}")
    public String finish(@ModelAttribute("booksToReadEnglishInput") Input input) {
        super.finish(input, new FinishedBookEnglish());
        return "redirect:/bookstoread/english/";
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        super.sort(SortBy.valueOf(property.toUpperCase()));
        return "redirect:/bookstoread/english/";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("booksToReadEnglishInput") Input input) {
        super.load(input, Language.ENGLISH);
        return "redirect:/bookstoread/english/";
    }

    @PostMapping("/loadBatch")
    public String loadBatch(@ModelAttribute("booksToReadEnglishInput") Input input) {
        super.loadBatch(input, Language.ENGLISH);
        return "redirect:/bookstoread/english/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("booksToReadEnglishInput") Input input) {
        super.save(input, Language.ENGLISH);
        return "redirect:/bookstoread/english/";
    }

    @GetMapping("/")
    public String list(Model model) {
        super.list(model, new ArrayList<>());
        return "booksToReadEnglish";
    }
}
