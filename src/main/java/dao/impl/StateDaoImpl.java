package dao.impl;

import dao.IStateDao;
import dao.connection.DatabaseConnection;
import dao.factory.StateFactory;
import entities.State;

import java.sql.*;

public class StateDaoImpl implements IStateDao {

    private Connection conn;

    public StateDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public State getStateByInitials(String initials) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        State state = null;
        try {
            String query = "SELECT `s`.stateId, `s`.initials " +
                           "FROM state `s`" +
                           "WHERE `s`.initials = ?";

            ps = conn.prepareStatement(query);
            ps.setString(1, initials);
            rs = ps.executeQuery();

            if (rs.next()) {
                state = StateFactory.getStateFromResultSet(rs);
            }

            return state;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public Integer insertState(State state) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer id = null;

        try {
            String sqlCommand = "INSERT INTO state VALUES(NULL, ?)";
            ps = conn.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, state.getInitials());
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
