package qiwi.controllers.english;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.BookToReadController;
import qiwi.input.PathInput;
import qiwi.model.book.BookToRead;
import qiwi.model.book.FinishedBook;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;

import java.util.ArrayList;

import static qiwi.util.enums.Language.ENGLISH;

@Controller
@RequestMapping("/bookstoread/english")
@SessionAttributes(types = Integer.class, names = "id")
public class BookToReadEnglishController extends BookToReadController {
    private final Language language = ENGLISH;

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("book", new BookToRead());

        return "books-to-read/english/add-book";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("book") BookToRead book, BindingResult result, Model model) {
        return super.add(book, result, model, language);
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("book", service.getBookById(id));

        return "books-to-read/english/edit-book";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("book") BookToRead book, BindingResult result, Model model) {
        return super.edit(book, result, model, language);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBookById(id);
        return "redirect:/bookstoread/english/";
    }

    @GetMapping("/finish/{id}")
    public String finish(@PathVariable Integer id, Model model) {
        BookToRead book = service.getBookById(id);

        FinishedBook bookToFinish = new FinishedBook();
        bookToFinish.setAuthor(book.getAuthor());
        bookToFinish.setName(book.getName());
        bookToFinish.setFound(book.getFound());
        bookToFinish.setDescription(book.getDescription());
        bookToFinish.setLanguage(book.getLanguage());
        bookToFinish.setAdditionalDates(new ArrayList<>());

        model.addAttribute("book", bookToFinish);
        model.addAttribute("id", book.getId());

        return "books-to-read/english/finish-book";
    }

    @PostMapping("/finish")
    public String finish(@ModelAttribute("book") FinishedBook book, @SessionAttribute("id") Integer id) {
        finishedBookService.addBook(book);

        service.deleteBookById(id);

        return "redirect:/bookstoread/english/";
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        super.sort(SortBy.valueOf(property.toUpperCase()));
        return "redirect:/bookstoread/english/";
    }

    @GetMapping("/load")
    public String load(Model model) {
        model.addAttribute("path", new PathInput());
        return "books-to-read/english/load";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("path") PathInput input) {
        super.load(input, language);
        return "redirect:/bookstoread/english/";
    }

    @GetMapping("/loadBatch")
    public String loadBatch(Model model) {
        model.addAttribute("path", new PathInput());
        return "books-to-read/english/load-batch";
    }

    @PostMapping("/loadBatch")
    public String loadBatch(@ModelAttribute("path") PathInput input) {
        super.loadBatch(input, language);
        return "redirect:/bookstoread/english/";
    }

    @GetMapping("/save")
    public String save(Model model) {
        model.addAttribute("path", new PathInput());
        return "books-to-read/english/save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("path") PathInput input) {
        super.save(input, language);
        return "redirect:/bookstoread/english/";
    }

    @GetMapping("/")
    public String showAllBooks(Model model) {
        setUpView(model, language);
        return "books-to-read/" + language.toLowerCase() + "/index";
    }
}