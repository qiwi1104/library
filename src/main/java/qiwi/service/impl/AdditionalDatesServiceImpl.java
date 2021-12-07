package qiwi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qiwi.model.AdditionalDates;
import qiwi.repository.AdditionalDatesRepository;

import java.util.List;

@Service
public class AdditionalDatesServiceImpl {
    @Autowired
    private AdditionalDatesRepository additionalDatesRepository;

    public void addDates(AdditionalDates additionalDates) {
        additionalDatesRepository.save(additionalDates);
    }

    public void deleteDates(Integer id) {
        try {
            additionalDatesRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AdditionalDates getDatesById(Integer id) {
        if (id <= additionalDatesRepository.count())
            return additionalDatesRepository.getOne(id);
        else return null;
    }

    public boolean exists(AdditionalDates additionalDate) {
        return additionalDatesRepository.findAll().contains(additionalDate);
    }

    public List<AdditionalDates> findAll() {
        return additionalDatesRepository.findAll();
    }

    public void computeIds() {
        additionalDatesRepository.computeIds();
    }
}
