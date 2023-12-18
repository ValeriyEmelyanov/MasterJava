package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

public class CityTestData {
    public static City CITY1;
    public static City CITY2;
    public static City CITY3;
    public static List<City> CITIES;

    public static void init() {
        CITY1 = new City("Москва");
        CITY2 = new City("Краснодар");
        CITY3 = new City("Подольск");
        CITIES = ImmutableList.of(CITY2, CITY1, CITY3);
    }

    public static void setUp() {
        CityDao dao = DBIProvider.getDao(CityDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            CITIES.forEach(dao::insert);
        });
    }
}
