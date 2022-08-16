package qiwi.controllers.english;

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

import static qiwi.model.enums.Language.ENGLISH;

@Controller
@RequestMapping("/bookstoread/english")
@SessionAttributes(types = Integer.class, names = "id")
public class BookToReadEnglishController extends BookToReadController {
    private final Language language = ENGLISH;

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("book", new BookToRead());

        return "books-to-read/" + language.toLowerCase() + "/add-book";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("book") BookToRead book, BindingResult result, Model model) {
        return bookToReadService.addBook(book, result, model, language)
                ? "redirect:/bookstoread/" + language.toLowerCase() + "/"
                : "books-to-read/" + language.toLowerCase() + "/add-book";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("book", bookToReadDAO.getBookById(id));

        return "books-to-read/" + language.toLowerCase() + "/edit-book";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("book") BookToRead book, BindingResult result, Model model) {
        return bookToReadService.editBook(book, result, model, language)
                ? "redirect:/bookstoread/" + language.toLowerCase() + "/"
                : "books-to-read/" + language.toLowerCase() + "/edit-book";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        bookToReadDAO.deleteBookById(id);
        return "redirect:/bookstoread/" + language.toLowerCase() + "/";
    }

    @GetMapping("/finish/{id}")
    public String finish(@PathVariable Integer id, Model model) {
        BookToRead book = bookToReadDAO.getBookById(id);

        model.addAttribute("book", new FinishedBook(book));
        model.addAttribute("id", book.getId());

        return "books-to-read/" + language.toLowerCase() + "/finish-book";
    }

    @PostMapping("/finish")
    public String finish(@ModelAttribute("book") FinishedBook book, @SessionAttribute("id") Integer id) {
        bookToReadService.finishBook(book, id);
        return "redirect:/bookstoread/" + language.toLowerCase() + "/";
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        bookToReadService.sort(SortBy.valueOf(property.toUpperCase()));
        return "redirect:/bookstoread/" + language.toLowerCase() + "/";
    }

    @GetMapping("/load")
    public String load(Model model) {
        model.addAttribute("path", new PathDTO());
        return "books-to-read/" + language.toLowerCase() + "/load";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("path") PathDTO input) {
        bookToReadService.load(input, language);
        return "redirect:/bookstoread/" + language.toLowerCase() + "/";
    }

    @GetMapping("/loadBatch")
    public String loadBatch(Model model) {
        model.addAttribute("path", new PathDTO());
        return "books-to-read/" + language.toLowerCase() + "/load-batch";
    }

    @PostMapping("/loadBatch")
    public String loadBatch(@ModelAttribute("path") PathDTO input) {
        bookToReadService.loadBatch(input, language);
        return "redirect:/bookstoread/" + language.toLowerCase() + "/";
    }

    @GetMapping("/save")
    public String save(Model model) {
        model.addAttribute("path", new PathDTO());
        return "books-to-read/" + language.toLowerCase() + "/save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("path") PathDTO input) {
        bookToReadService.save(input, language);
        return "redirect:/bookstoread/" + language.toLowerCase() + "/";
    }

    @GetMapping("/")
    public String showAllBooks(Model model) {
        bookToReadService.setUpView(model, language);
        return "books-to-read/" + language.toLowerCase() + "/index";
    }
}