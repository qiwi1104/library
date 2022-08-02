package qiwi.controllers.spanish;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.BookToReadController;
import qiwi.dto.PathDTO;
import qiwi.model.book.BookToRead;
import qiwi.model.book.FinishedBook;
import qiwi.model.enums.Language;
import qiwi.model.enums.SortBy;

import static qiwi.model.enums.Language.SPANISH;

@Controller
@RequestMapping("/bookstoread/spanish")
@SessionAttributes(types = Integer.class, names = "id")
public class BookToReadSpanishController extends BookToReadController {
    private final Language language = SPANISH;

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("book", new BookToRead());

        return "books-to-read/spanish/add-book";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("book") BookToRead book, BindingResult result, Model model) {
        return service.addBook(book, result, model, language);
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("book", service.getBookById(id));

        return "books-to-read/spanish/edit-book";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("book") BookToRead book, BindingResult result, Model model) {
        return service.editBook(book, result, model, language);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBookById(id);
        return "redirect:/bookstoread/spanish/";
    }

    @GetMapping("/finish/{id}")
    public String finish(@PathVariable Integer id, Model model) {
        BookToRead book = service.getBookById(id);

        model.addAttribute("book", new FinishedBook(book));
        model.addAttribute("id", book.getId());

        return "books-to-read/spanish/finish-book";
    }

    @PostMapping("/finish")
    public String finish(@ModelAttribute("book") FinishedBook book, @SessionAttribute("id") Integer id) {
        finishedBookService.addBook(book);

        service.deleteBookById(id);

        return "redirect:/bookstoread/spanish/";
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        service.sort(SortBy.valueOf(property.toUpperCase()));
        return "redirect:/bookstoread/spanish/";
    }

    @GetMapping("/load")
    public String load(Model model) {
        model.addAttribute("path", new PathDTO());
        return "books-to-read/spanish/load";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("path") PathDTO input) {
        service.load(input, language);
        return "redirect:/bookstoread/spanish/";
    }

    @GetMapping("/loadBatch")
    public String loadBatch(Model model) {
        model.addAttribute("path", new PathDTO());
        return "books-to-read/spanish/load-batch";
    }

    @PostMapping("/loadBatch")
    public String loadBatch(@ModelAttribute("path") PathDTO input) {
        service.loadBatch(input, language);
        return "redirect:/bookstoread/spanish/";
    }

    @GetMapping("/save")
    public String save(Model model) {
        model.addAttribute("path", new PathDTO());
        return "books-to-read/spanish/save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("path") PathDTO input) {
        service.save(input, language);
        return "redirect:/bookstoread/spanish/";
    }

    @GetMapping("/")
    public String showAllBooks(Model model) {
        service.setUpView(model, language);
        return "books-to-read/" + language.firstLetterToUpperCase() + "/index";
    }
}