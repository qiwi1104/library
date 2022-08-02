package qiwi.controllers.common;

import org.springframework.beans.factory.annotation.Autowired;
import qiwi.service.impl.FinishedBookServiceImpl;

public abstract class FinishedBookController extends BookController {
    @Autowired
    protected FinishedBookServiceImpl service;
}
