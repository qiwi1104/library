package qiwi.controllers.english;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.BookToReadController;
import qiwi.model.common.input.Input;
import qiwi.model.common.input.PathInput;
import qiwi.model.english.BookToReadEnglish;
import qiwi.model.english.FinishedBookEnglish;
import qiwi.service.english.BookToReadEnglishServiceImpl;
import qiwi.service.english.FinishedBookEnglishServiceImpl;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;

@Controller
@RequestMapping("/bookstoread/english")
public class BookToReadEnglishController extends BookToReadController<
        BookToReadEnglish,
        BookToReadEnglishServiceImpl,
        FinishedBookEnglish,
        FinishedBookEnglishServiceImpl> {

    @PostMapping("/add")
    public String add(@ModelAttribute("booksToReadEnglishInput") Input input, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return showTable(input, model, "English");
        }

        if (super.add(input, model, new BookToReadEnglish())) {
            return "redirect:/bookstoread/english/";
        } else {
            return showTable(input, model, "English");
        }
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("booksToReadEnglishInput") Input input, BindingResult result, Model model) {
        return getRedirectionAddress(input, result, model, "English", "bookstoread");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBook(id);
        return "redirect:/bookstoread/english/";
    }

    @PostMapping("/finish/{id}")
    public String finish(@ModelAttribute("booksToReadEnglishInput") Input input, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return showTable(input, model, "English");
        }

        if (super.finish(input, model, new FinishedBookEnglish())) {
            return "redirect:/bookstoread/english/";
        } else {
            return showTable(input, model, "English");
        }
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        super.sort(SortBy.valueOf(property.toUpperCase()));
        return "redirect:/bookstoread/english/";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("booksToReadEnglishInput") PathInput input, BindingResult result, Model model) {
        super.load(input, Language.ENGLISH);
        return "redirect:/bookstoread/english/";
    }

    @PostMapping("/loadBatch")
    public String loadBatch(@ModelAttribute("booksToReadEnglishInput") PathInput input, BindingResult result, Model model) {
        super.loadBatch(input, Language.ENGLISH);
        return "redirect:/bookstoread/english/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("booksToReadEnglishInput") PathInput input, BindingResult result, Model model) {
        super.save(input, Language.ENGLISH);
        return "redirect:/bookstoread/english/";
    }

    @GetMapping("/")
    public String list(Model model) {
        return showTable(new Input(), model, "English");
    }
}
