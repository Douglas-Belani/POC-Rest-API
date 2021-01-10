package dao.impl;

import dao.ICategoryDao;
import dao.connection.DatabaseConnection;
import dao.factory.CategoryFactory;
import entities.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoImpl implements ICategoryDao {

    private Connection conn;

    public CategoryDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Category> getAllCategories() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Category> categories = new ArrayList<>();

        try {
            String query = "SELECT `ca`.categoryId, `ca`.categoryName " +
                           "FROM category `ca` ";

            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();

            if (rs.next()) {
                while (!rs.isAfterLast()) {
                    categories.add(CategoryFactory.getCategoryFromResultSet(rs));
                    rs.next();
                }
            }

            return categories;

        } catch (SQLException e) {
            return categories;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public Category getCategoryById(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Category category = null;

        try {
            String query = "SELECT `ca`.categoryId, `ca`.categoryName " +
                           "FROM category `ca` " +
                           "WHERE `ca`.categoryId = ?";

            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                category = CategoryFactory.getCategoryFromResultSet(rs);
            }

            return category;

        } catch (SQLException e) {
            return null;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public Integer insertCategory(Category category) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer id = null;

        try {
            String sqlCommand = "INSERT INTO category VALUES (NULL, ?)";
            ps = conn.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, category.getName());
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
    public boolean updateCategory(Category category) {
        PreparedStatement ps = null;

        try {
            String sqlCommand = "UPDATE category SET name = ? WHERE categoryId = ?";
            ps = conn.prepareStatement(sqlCommand);
            ps.setInt(1, category.getCategoryId());

            int rowsAffected = ps.executeUpdate();

            return rowsAffected == 1;

        } catch (SQLException e) {
            return false;

        } finally {
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public boolean deleteCategoryById(int id) {
        PreparedStatement ps = null;

        try {
            String sqlCommand = "DELETE FROM category WHERE categoryId = ?";
            ps = conn.prepareStatement(sqlCommand);
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected == 1;

        } catch (SQLException e) {
            return false;

        } finally {
            DatabaseConnection.closePreparedStatement(ps);
        }
    }
}
