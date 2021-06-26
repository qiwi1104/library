package qiwi.service;

import qiwi.model.english.AdditionalDates;

import java.util.List;

public interface AdditionalDatesService<T> {
    void addDates(AdditionalDates additionalDates);
    void deleteDates(Integer id);
    T getDatesById(Integer id);
    List<T> findAll();
}
