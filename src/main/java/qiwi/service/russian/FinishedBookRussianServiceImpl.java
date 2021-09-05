package qiwi.service.russian;

import org.springframework.stereotype.Service;
import qiwi.model.russian.FinishedBookRussian;
import qiwi.repository.russian.FinishedBookRussianRepository;
import qiwi.service.common.FinishedBookServiceImpl;

@Service
public class FinishedBookRussianServiceImpl extends FinishedBookServiceImpl<FinishedBookRussian, FinishedBookRussianRepository> {
}
