package dao.impl;

import dao.IUserDao;
import dao.connection.DatabaseConnection;
import dao.factory.UserFactory;
import entities.User;
import java.sql.*;

public class UserDaoImpl implements IUserDao {

    private Connection conn;

    public UserDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public User getUserById(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;

        try {
            String query = "SELECT `u`.userId, `u`.cpf,`u`.userName, `u`.email, `u`.password, `u`.birthDate " +
                           "FROM `user` `u` " +
                           "WHERE `u`.userId = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                user = UserFactory.getUserFromResultSet(rs);
            }

            return user;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public User getUserByEmail(String email) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;

        try {
            String query = "SELECT `u`.userId, `u`.cpf,`u`.userName, `u`.email, `u`.password, `u`.birthDate " +
                           "FROM `user` `u` " +
                           "WHERE `u`.email = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                user = UserFactory.getUserFromResultSet(rs);
            }

            return user;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public Integer insertUser(User user) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer id = null;

        try {
            String sqlCommand = "INSERT INTO `user` VALUES(NULL, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getCpf());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setDate(5, Date.valueOf(user.getBirthDate()));
            ps.execute();
            rs = ps.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
            }

            return id;

        } catch (SQLException e) {
            e.printStackTrace();
            return id;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public boolean updateUser(User user, int id) {
        PreparedStatement ps = null;

        try {
            String sqlCommand = "UPDATE `user` " +
                                "SET userName = ?, email = ?, password = ?, birthDate = ?" +
                                "WHERE `user`.userId = ?";

            ps = conn.prepareStatement(sqlCommand);
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setDate(4, Date.valueOf(user.getBirthDate()));
            ps.setInt(5, id);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public boolean deleteUser(int id) {
        PreparedStatement ps = null;

        try {
            String sqlCommand = "DELETE FROM `user` WHERE userId = ?";
            ps = conn.prepareStatement(sqlCommand);
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            DatabaseConnection.closePreparedStatement(ps);
        }
    }
}
