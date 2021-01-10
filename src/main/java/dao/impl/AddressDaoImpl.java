package dao.impl;

import dao.IAddressDao;
import dao.connection.DatabaseConnection;
import dao.factory.AddressFactory;
import dao.factory.CityFactory;
import dao.factory.StateFactory;
import entities.Address;
import entities.City;
import entities.State;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDaoImpl implements IAddressDao {

    private Connection conn;

    public AddressDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Address getAddressById(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Address address = null;

        try {
            String query = "SELECT `a`.addressId, `a`.neighborhood, `a`.number, `a`.zipCode, " +
                           "`a`.street, `a`.complement, " +
                           "`ci`.cityId, `ci`.cityName, " +
                           "`s`.stateId, `s`.initials " +
                           "FROM address `a` " +
                           "INNER JOIN city `ci` ON `a`.cityId = `ci`.cityId " +
                           "INNER JOIN state `s` ON `ci`.stateId = `s`.stateId " +
                           "WHERE `a`.addressId = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                State state = StateFactory.getStateFromResultSet(rs);
                City city = CityFactory.getCityFromResultSet(rs, state);
                address = AddressFactory.getAddressFromResultSet(rs, city);
            }

            return address;

        } catch (SQLException e) {
            return null;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public List<Address> getAllAddressesByUserId(int userId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Address> addresses = new ArrayList<>();

        try {
            String query = "SELECT `a`.addressId, `a`.neighborhood, `a`.number, `a`.zipCode, " +
                           "`a`.street, `a`.complement, " +
                           "`ci`.cityId, `ci`.cityName, " +
                           "`s`.stateId, `s`.initials " +
                           "FROM address `a` " +
                           "INNER JOIN city `ci` ON `a`.cityId = `ci`.cityId " +
                           "INNER JOIN state `s` ON `ci`.stateId = `s`.stateId " +
                           "WHERE `a`.userId = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            if (rs.next()) {
                while (!rs.isAfterLast()) {
                    State state = StateFactory.getStateFromResultSet(rs);
                    City city = CityFactory.getCityFromResultSet(rs, state);
                    addresses.add(AddressFactory.getAddressFromResultSet(rs, city));
                    rs.next();
                }
            }

            return addresses;

        } catch (SQLException e) {
            return addresses;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public Integer insertAddress(Address address, int userId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer id = null;

        try {
            String sqlCommand = "INSERT INTO address VALUES(NULL, ?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, address.getNeighborhood());
            ps.setString(2, address.getNumber());
            ps.setString(3, address.getZipCode());
            ps.setString(4, address.getStreet());
            ps.setString(5, address.getComplement());
            ps.setInt(6, userId);
            ps.setInt(7, address.getCity().getCityId());
            ps.execute();
            rs = ps.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
            }

            return id;

        } catch (SQLException e) {
            return null;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public boolean updateAddress(Address address) {
        PreparedStatement ps = null;

        try {
            String sqlCommand = "UPDATE address " +
                                "SET neighborhood = ?, number = ?, zipCode = ?, street = ?, " +
                                "complement = ?, cityId = ? " +
                                "WHERE addressId = ?";
            ps = conn.prepareStatement(sqlCommand);
            ps.setString(1, address.getNeighborhood());
            ps.setString(2, address.getNumber());
            ps.setString(3, address.getZipCode());
            ps.setString(4, address.getStreet());
            ps.setString(5, address.getComplement());
            ps.setInt(6, address.getCity().getCityId());
            ps.setInt(7, address.getAddressId());

            int rowsAffected = ps.executeUpdate();

            return rowsAffected == 1;

        } catch (SQLException e) {
            return false;

        } finally {
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public boolean deleteAddressById(int addressId) {
        PreparedStatement ps = null;

        try {
            String sqlCommand = "DELETE FROM address WHERE addressId = ?";
            ps = conn.prepareStatement(sqlCommand);
            ps.setInt(1, addressId);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected == 1;

        } catch (SQLException e) {
            return false;

        } finally {
            DatabaseConnection.closePreparedStatement(ps);
        }
    }
}
