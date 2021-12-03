package qiwi.service;

import qiwi.model.AdditionalDates;

import java.util.List;

public interface AdditionalDatesService {
    void addDates(AdditionalDates additionalDates);

    void deleteDates(Integer id);

    AdditionalDates getDatesById(Integer id);

    boolean exists(AdditionalDates additionalDate);

    List<AdditionalDates> findAll();
}
