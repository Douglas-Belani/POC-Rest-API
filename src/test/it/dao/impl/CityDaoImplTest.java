package dao.impl;

import connection.H2Connection;
import dao.ICityDao;
import entities.City;
import entities.State;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;


public class CityDaoImplTest {

    private static ICityDao cityDao;

    @BeforeClass
    public static void initialize() {
        cityDao = new CityDaoImpl(H2Connection.getH2DatabaseConnection());
    }

    @Test
    public void test_getCityByNameAndStateInitials_Should_Return_City_Id_1_State_Id_1_For_cidade1_SP() {
        String cityName = "city1";
        String stateInitials = "SP";

        State expectedState = new State(1, stateInitials);
        City expectedCity = new City(1, cityName, expectedState);

        City city = cityDao.getCityByNameAndStateInitials(cityName, stateInitials);
        assertThat(expectedCity, samePropertyValuesAs(city));
    }

    @Test
    public void test_getCityByNameAndStateInitials_Should_Return_Null_For_Nonexistent_City_Name() {
        String nonExistentCityName = "city10";
        String stateInitials = "SP";

        City city = cityDao.getCityByNameAndStateInitials(nonExistentCityName, stateInitials);

        Assert.assertNull(city);
    }

    @Test
    public void test_getCityByNameAndStateInitials_Should_Return_Null_For_Nonexistent_State_Initials() {
        String cityName = "city1";
        String nonExistentStateInitials = "AB";

        City city = cityDao.getCityByNameAndStateInitials(cityName, nonExistentStateInitials);

        Assert.assertNull(city);
    }

    @Test
    public void test_insertCity_Should_Return_City_Id_For_Successful_Insert() {
        State state = new State(1, "SP");
        City newCity = new City(null, "city5", state);

        Integer cityId = cityDao.insertCity(newCity);

        Assert.assertEquals(Integer.valueOf(5), cityId);
    }

}
