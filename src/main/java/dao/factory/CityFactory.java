package dao.factory;

import entities.City;
import entities.State;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CityFactory {

    public static City getCityFromResultSet(ResultSet rs, State state) throws SQLException {
        return new City(rs.getInt("cityId"), rs.getString("cityName"), state);
    }

}
