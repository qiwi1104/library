package qiwi.service.english;

import org.springframework.stereotype.Service;
import qiwi.model.english.FinishedBookEnglish;
import qiwi.repository.english.FinishedBookEnglishRepository;
import qiwi.service.common.FinishedBookServiceImpl;

@Service
public class FinishedBookEnglishServiceImpl extends FinishedBookServiceImpl<FinishedBookEnglish, FinishedBookEnglishRepository> {
}
