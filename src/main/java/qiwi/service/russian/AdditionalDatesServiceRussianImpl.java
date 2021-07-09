package qiwi.service.russian;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qiwi.model.russian.AdditionalDatesRussian;
import qiwi.repository.russian.AdditionalDatesRussianRepository;
import qiwi.service.AdditionalDatesService;

import java.util.List;

@Service
public class AdditionalDatesServiceRussianImpl implements AdditionalDatesService<AdditionalDatesRussian> {
    @Autowired
    private AdditionalDatesRussianRepository additionalDatesRepository;

    @Override
    public void addDates(AdditionalDatesRussian additionalDates) {
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
    public AdditionalDatesRussian getDatesById(Integer id) {
        return additionalDatesRepository.getOne(id);
    }

    @Override
    public List<AdditionalDatesRussian> findAll() {
        return additionalDatesRepository.findAll();
    }
}
