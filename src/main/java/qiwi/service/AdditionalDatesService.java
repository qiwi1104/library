package qiwi.service;

import qiwi.model.common.AdditionalDates;

import java.util.List;

public interface AdditionalDatesService<T extends AdditionalDates> {
    void addDates(T additionalDates);

    void deleteDates(Integer id);

    T getDatesById(Integer id);

    List<T> findAll();
}
