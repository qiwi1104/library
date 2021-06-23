package qiwi.controllers.english;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qiwi.model.english.FinishedBook;
import qiwi.service.FinishedBookEnglishServiceImpl;

import java.sql.Date;

@Controller
@RequestMapping("/finishedbooks/english")
public class FinishedBookController {
    @Autowired
    private FinishedBookEnglishServiceImpl service;

    @PostMapping(path = "/add")
    public
//    @ResponseBody
    String addBook(@RequestParam(defaultValue = "") String author, @RequestParam String name, @RequestParam Date start,
                   @RequestParam Date end, @RequestParam(defaultValue = "") String start_info,
                   @RequestParam(defaultValue = "") String end_info, @RequestParam Date found, @RequestParam String found_description) {

        FinishedBook book = new FinishedBook();
        book.setAuthor(author);
        book.setName(name);
        book.setStart(start);
        book.setEnd(end);
        book.setStartDescription(start_info);
        book.setEndDescription(end_info);
        book.setFound(found);
        book.setFoundDescription(found_description);

        return "redirect:/finishedbooks/english/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBook(id);

        return "redirect:/finishedbooks/english/";
    }

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("books", service.findAll());

        return "index";
    }
}