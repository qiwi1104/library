package qiwi.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qiwi.model.common.AdditionalDates;
import qiwi.repository.common.AdditionalDatesRepository;
import qiwi.service.AdditionalDatesService;

import java.util.List;

@Service
public abstract class AdditionalDatesServiceImpl<
        T extends AdditionalDates,
        S extends AdditionalDatesRepository<T>> implements AdditionalDatesService<T> {
    @Autowired
    private S additionalDatesRepository;

    @Override
    public void addDates(T additionalDates) {
        additionalDatesRepository.save(additionalDates);
    }

    @Override
    public void deleteDates(Integer id) {
        try {
            additionalDatesRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public T getDatesById(Integer id) {
        if (id <= additionalDatesRepository.count())
            return additionalDatesRepository.getOne(id);
        else return null;
    }

    @Override
    public boolean exists(T additionalDate) {
        for (T date : additionalDatesRepository.findAll()) {
            if (additionalDate.equals(date)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<T> findAll() {
        return additionalDatesRepository.findAll();
    }
}
