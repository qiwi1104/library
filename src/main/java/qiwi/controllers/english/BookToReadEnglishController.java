package qiwi.controllers.english;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.BookToReadController;
import qiwi.model.input.Input;
import qiwi.model.input.PathInput;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;

import static qiwi.util.enums.Language.ENGLISH;

@Controller
@RequestMapping("/bookstoread/english")
public class BookToReadEnglishController extends BookToReadController {
    private final Language language = ENGLISH;

    @PostMapping("/add")
    public String add(@ModelAttribute("booksToReadEnglishInput") Input input, BindingResult result, Model model) {
        return super.add(input, model, language);
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("booksToReadEnglishInput") Input input, BindingResult result, Model model) {
        return super.edit(input, model, language);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBookById(id);
        return "redirect:/bookstoread/english/";
    }

    @PostMapping("/finish/{id}")
    public String finish(@ModelAttribute("booksToReadEnglishInput") Input input, BindingResult result, Model model) {
        return super.finish(input, model, language, result.hasErrors());
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        super.sort(SortBy.valueOf(property.toUpperCase()));
        return "redirect:/bookstoread/english/";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("booksToReadEnglishInput") PathInput input) {
        super.load(input, language);
        return "redirect:/bookstoread/english/";
    }

    @PostMapping("/loadBatch")
    public String loadBatch(@ModelAttribute("booksToReadEnglishInput") PathInput input) {
        super.loadBatch(input, language);
        return "redirect:/bookstoread/english/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("booksToReadEnglishInput") PathInput input) {
        super.save(input, language);
        return "redirect:/bookstoread/english/";
    }

    @GetMapping("/")
    public String showAllBooks(Model model) {
        setUpView(model, language);
        return "booksToRead" + language.firstLetterToUpperCase();
    }
}