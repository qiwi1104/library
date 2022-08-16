package qiwi.controllers.common;

import org.springframework.beans.factory.annotation.Autowired;
import qiwi.service.dao.FinishedBookDAO;
import qiwi.service.service.FinishedBookService;

public abstract class FinishedBookController extends BookController {
    @Autowired
    protected FinishedBookService finishedBookService;
    @Autowired
    protected FinishedBookDAO finishedBookDAO;
}
