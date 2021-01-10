package dao.factory;

import entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserFactory {

    public static User getUserFromResultSet(ResultSet rs) throws SQLException {
        return new User(rs.getInt("userId"), rs.getString("userName"),
                rs.getString("cpf"),rs.getString("email"),
                rs.getString("password"), rs.getDate("birthDate").toLocalDate());
    }

}
