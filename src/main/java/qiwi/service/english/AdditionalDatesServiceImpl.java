package qiwi.service.english;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qiwi.model.english.AdditionalDates;
import qiwi.repository.english.AdditionalDatesRepository;
import qiwi.service.AdditionalDatesService;

import java.util.List;

@Service
public class AdditionalDatesServiceImpl implements AdditionalDatesService<AdditionalDates> {
    @Autowired
    private AdditionalDatesRepository additionalDatesRepository;

    @Override
    public void addDates(AdditionalDates additionalDates) {
        additionalDatesRepository.save(additionalDates);
    }

    @Override
    public void deleteDates(Integer id) {
        try {
            additionalDatesRepository.deleteById(id);
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    @Override
    public AdditionalDates getDatesById(Integer id) {
        return additionalDatesRepository.getOne(id);
    }

    @Override
    public List<AdditionalDates> findAll() {
        return additionalDatesRepository.findAll();
    }
}
