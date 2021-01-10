package dao.impl;

import dao.ICityDao;
import dao.connection.DatabaseConnection;
import dao.factory.CityFactory;
import dao.factory.StateFactory;
import entities.City;
import entities.State;

import java.sql.*;

public class CityDaoImpl implements ICityDao {

    private Connection conn;

    public CityDaoImpl(Connection conn) {
        this.conn = conn;
    }


    @Override
    public City getCityByNameAndStateInitials(String name, String initials) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        City city = null;

        try {
            String query = "SELECT `ci`.cityId, `ci`.cityName, " +
                           "`s`.stateId, `s`.initials " +
                           "FROM city `ci` " +
                           "INNER JOIN state `s` ON `ci`.stateId = `s`.stateId " +
                           "WHERE `ci`.cityName = ? AND `s`.initials = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, initials);
            rs = ps.executeQuery();

            if (rs.next()) {
                State state = StateFactory.getStateFromResultSet(rs);
                city = CityFactory.getCityFromResultSet(rs, state);
            }

            return city;

        } catch (SQLException e) {
            return null;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public Integer insertCity(City city) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer id = null;

        try {
            String sqlCommand = "INSERT INTO city VALUES (NULL, ?, ?)";
            ps = conn.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, city.getName().toUpperCase());
            ps.setInt(2, city.getState().getStateId());
            ps.execute();
            rs = ps.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
            }

            return id;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }
}
