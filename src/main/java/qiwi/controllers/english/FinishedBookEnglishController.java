package qiwi.controllers.english;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qiwi.IO;
import qiwi.TimeFormat;
import qiwi.controllers.CommonActions;
import qiwi.model.common.Input;
import qiwi.model.english.AdditionalDatesEnglish;
import qiwi.model.english.FinishedBookEnglish;
import qiwi.service.english.AdditionalDatesEnglishServiceImpl;
import qiwi.service.english.FinishedBookEnglishServiceImpl;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/finishedbooks/english")
public class FinishedBookEnglishController {
    @Autowired
    private FinishedBookEnglishServiceImpl service;
    @Autowired
    private AdditionalDatesEnglishServiceImpl additionalDatesService;

    private String sortDateMethod = "ASC";
    private String sortProperty = "start";

    private List<FinishedBookEnglish> filterAndSort() {
        List<FinishedBookEnglish> books = null;

        switch (sortDateMethod) {
            case "ASC":
                switch (sortProperty) {
                    case "id":
                        books = service.findAllByOrderByIdAsc();
                        break;
                    case "start":
                        books = service.findAllByOrderByStartAsc();
                        break;
                    case "end":
                        books = service.findAllByOrderByEndAsc();
                        break;
                }
                break;
            case "DESC":
                switch (sortProperty) {
                    case "id":
                        books = service.findAllByOrderByIdDesc();
                        break;
                    case "start":
                        books = service.findAllByOrderByStartDesc();
                        break;
                    case "end":
                        books = service.findAllByOrderByEndDesc();
                        break;
                }
                break;
        }
        return books;
    }

    private List<FinishedBookEnglish> fillList(JSONArray source) {
        List<FinishedBookEnglish> destination = new ArrayList<>();

        for (int i = 0; i < source.length(); i++) {
            FinishedBookEnglish bookToAdd = new FinishedBookEnglish();

            bookToAdd.setId(i + 1);
            bookToAdd.setAuthor(source.getJSONObject(i).get("author").toString());
            bookToAdd.setName(source.getJSONObject(i).get("name").toString());

            try {
                String date = TimeFormat.formatTime("M/d/yy", "yyyy-M-d",
                        source.getJSONObject(i).getJSONArray("dates").get(0).toString());

                bookToAdd.setStart(Date.valueOf(date));
            } catch (Exception e) {
                bookToAdd.setDescription("start: " + source.getJSONObject(i).getJSONArray("dates").get(0).toString()
                        + "\n" + source.getJSONObject(i).get("start_description").toString());

                bookToAdd.setStart(Date.valueOf("1970-1-1"));
            }

            try {
                String date = TimeFormat.formatTime("M/d/yy", "yyyy-M-d",
                        source.getJSONObject(i).getJSONArray("dates").get(1).toString());

                bookToAdd.setEnd(Date.valueOf(date));
            } catch (Exception e) {
                bookToAdd.setDescription("end: " + source.getJSONObject(i).getJSONArray("dates").get(1).toString()
                        + "\n" + source.getJSONObject(i).get("end_description").toString());

                bookToAdd.setEnd(Date.valueOf("1970-1-1"));
            }

            try {
                String date = TimeFormat.formatTime("M/d/yy", "yyyy-M-d", source.getJSONObject(i).get("found").toString());
                bookToAdd.setFound(Date.valueOf(date));
            } catch (Exception e) {
                bookToAdd.setFound(Date.valueOf("1970-1-1"));
            }

            bookToAdd.setDescription(source.getJSONObject(i).get("description").toString());

            destination.add(bookToAdd);
        }

        return destination;
    }

    private void fillAdditionalDatesTable(JSONArray source) throws ParseException {
        for (int i = 0; i < source.length(); i++) {
            if (source.getJSONObject(i).getJSONObject("additional_dates").getJSONArray("start").length() != 0) {
                for (FinishedBookEnglish finishedBook : service.findAll()) {
                    if (finishedBook.getName().equals(source.getJSONObject(i).get("name"))) {
                        AdditionalDatesEnglish additionalDates = new AdditionalDatesEnglish();

                        additionalDates.setId(additionalDatesService.findAll().size() + 1);
                        additionalDates.setFinishedBookId(finishedBook.getId());
                        for (int j = 0; j < source.getJSONObject(i).getJSONObject("additional_dates").getJSONArray("start").length(); j++) {
                            String date = TimeFormat.formatTime("M/d/yy", "yyyy-M-d",
                                    source.getJSONObject(i).getJSONObject("additional_dates").getJSONArray("start").get(j).toString());

                            String date2 = TimeFormat.formatTime("M/d/yy", "yyyy-M-d",
                                    source.getJSONObject(i).getJSONObject("additional_dates").getJSONArray("end").get(j).toString());

                            additionalDates.setStart(Date.valueOf(date));
                            additionalDates.setEnd(Date.valueOf(date2));
                            additionalDatesService.addDates(additionalDates);
                        }
                    }
                }
            }
        }
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("finishedEnglishInput") Input inputFinished) {
        FinishedBookEnglish book = new FinishedBookEnglish();

        book.setId(service.findAll().size() + 1);
        CommonActions.setBookAttributesFromInput(book, inputFinished, "addFirst");

        if (service.isInTable(book)) {
            additionalDatesService.addDates(new AdditionalDatesEnglish(additionalDatesService.findAll().size() + 1,
                    book.getId(), book.getStart(), book.getEnd()));
        } else {
            CommonActions.setBookAttributesFromInput(book, inputFinished, "addSecond");

            service.addBook(book);
        }

        return "redirect:/finishedbooks/english/";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("finishedEnglishInput") Input inputFinished) {
        FinishedBookEnglish book = service.getBookById(inputFinished.getId());

        CommonActions.setBookAttributesFromInput(book, inputFinished, "edit");

        service.addBook(book);

        return "redirect:/finishedbooks/english/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBook(id);

        return "redirect:/finishedbooks/english/";
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        sortDateMethod = sortDateMethod.equals("ASC") ? "DESC" : "ASC";
        sortProperty = property;

        return "redirect:/finishedbooks/english/";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("finishedEnglishInput") Input input) throws IOException, ParseException {
        service.clearAll();
        List<FinishedBookEnglish> finishedBooks;

        JSONArray jsonBooks = IO.readJSONFile(input.getName());
        finishedBooks = fillList(jsonBooks);

        service.addAll(finishedBooks);
        fillAdditionalDatesTable(jsonBooks); // добавляет доп. даты в свою таблицу

        return "redirect:/finishedbooks/english/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("finishedEnglishInput") Input input) {
        List<FinishedBookEnglish> bookToReadList = service.findAll();
        List<AdditionalDatesEnglish> additionalDatesList = additionalDatesService.findAll();

        CommonActions.saveTableToJSON(bookToReadList, additionalDatesList, input.getName(), "English");

        return "redirect:/finishedbooks/english/";
    }

    @GetMapping("/")
    public String list(Model model) {
        List<FinishedBookEnglish> bookList = filterAndSort();

        model.addAttribute("books", bookList);
        model.addAttribute("additionalDates", additionalDatesService.findAll());

        return "finishedBooksEnglish";
    }
}