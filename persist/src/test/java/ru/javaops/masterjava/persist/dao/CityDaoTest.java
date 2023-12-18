package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.CityTestData;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

import static ru.javaops.masterjava.persist.CityTestData.CITIES;

public class CityDaoTest extends AbstractDaoTest<CityDao>{

    public CityDaoTest() {
        super(CityDao.class);
    }

    @BeforeClass
    public static void init() {
        CityTestData.init();
    }

    @Before
    public void setUp() {
        CityTestData.setUp();
    }

    @Test
    public void getWithLimit() {
        List<City> cities = dao.getWithLimit(3);
        Assert.assertEquals(CITIES, cities);
    }

    @Test
    public void insertBatch() {
        dao.clean();
        Assert.assertEquals(0, dao.getWithLimit(100).size());
        dao.insertBatch(CITIES, 100);
        Assert.assertEquals(CITIES.size(), dao.getWithLimit(100).size());
    }
}
