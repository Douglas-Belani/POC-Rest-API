package dao.factory;

import entities.Address;
import entities.City;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressFactory {

    public static Address getAddressFromResultSet(ResultSet rs, City city) throws SQLException {
        return new Address(rs.getInt("addressId"), rs.getString("neighborhood"),
                rs.getString("number"), rs.getString("zipCode"),
                rs.getString("street"), rs.getString("complement"), city);
    }

}
