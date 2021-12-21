package qiwi.controllers.spanish;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.BookToReadController;
import qiwi.model.input.Input;
import qiwi.model.input.PathInput;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;

import static qiwi.util.enums.Language.SPANISH;

@Controller
@RequestMapping("/bookstoread/spanish")
public class BookToReadSpanishController extends BookToReadController {
    private final Language language = SPANISH;

    @PostMapping("/add")
    public String add(@ModelAttribute("booksToReadSpanishInput") Input input, BindingResult result, Model model) {
        return super.add(input, model, language);
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("booksToReadSpanishInput") Input input, BindingResult result, Model model) {
        return super.edit(input, model, language);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBookById(id);
        return "redirect:/bookstoread/spanish/";
    }

    @PostMapping("/finish/{id}")
    public String finish(@ModelAttribute("booksToReadSpanishInput") Input input, BindingResult result, Model model) {
        return super.finish(input, model, language, result.hasErrors());
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        super.sort(SortBy.valueOf(property.toUpperCase()));
        return "redirect:/bookstoread/spanish/";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("booksToReadSpanishInput") PathInput input) {
        super.load(input, language);
        return "redirect:/bookstoread/spanish/";
    }

    @PostMapping("/loadBatch")
    public String loadBatch(@ModelAttribute("booksToReadSpanishInput") PathInput input) {
        super.loadBatch(input, language);
        return "redirect:/bookstoread/spanish/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("booksToReadSpanishInput") PathInput input) {
        super.save(input, language);
        return "redirect:/bookstoread/spanish/";
    }

    @GetMapping("/")
    public String showAllBooks(Model model) {
        setUpView(model, language);
        return "booksToRead" + language.firstLetterToUpperCase();
    }
}