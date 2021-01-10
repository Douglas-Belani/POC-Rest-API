package dao.factory;

import entities.State;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StateFactory {

    public static State getStateFromResultSet(ResultSet rs) throws SQLException {
        return new State(rs.getInt("stateId"), rs.getString("initials"));
    }

}
