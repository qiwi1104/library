package qiwi.controllers.russian;

import org.springframework.ui.Model;
import qiwi.model.english.FinishedBook;
import qiwi.repository.english.FinishedBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BookRussianController {
    @Autowired
    private FinishedBookRepository russianFinishedBookRepository;
    private List<FinishedBook> russianBookList = new ArrayList<>();

    private void refreshBookList() {
        russianBookList.clear();
        russianBookList.addAll(russianFinishedBookRepository.findAll());
    }

    @PostMapping(path = "/russianBooks/add")
    public @ResponseBody
    String addBook(@RequestParam(defaultValue = "") String author, @RequestParam String name, @RequestParam Date start,
                   @RequestParam Date end, @RequestParam(defaultValue = "") String start_info,
                   @RequestParam(defaultValue = "") String end_info, @RequestParam Date found) {

        FinishedBook book = new FinishedBook();
        book.setAuthor(author);
        book.setName(name);
        book.setStart(start);
        book.setEnd(end);
        book.setStartDescription(start_info);
        book.setEndDescription(end_info);
        book.setFound(found);

        return "Saved";
    }

    @GetMapping(path = "/russianBooks/delete/{id}")
    public String delete(@PathVariable Integer id) {
        russianFinishedBookRepository.deleteById(id);
        refreshBookList();

        return "redirect:/";
    }

    @GetMapping("/russianBooks")
    public String list(Model model) {
        refreshBookList();
        model.addAttribute("russianBooks", russianBookList);

        return "index";
    }
}