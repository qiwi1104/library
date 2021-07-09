package qiwi.service;

import java.util.List;

public interface AdditionalDatesService<T> {
    void addDates(T additionalDates);

    void deleteDates(Integer id);

    T getDatesById(Integer id);

    List<T> findAll();
}
