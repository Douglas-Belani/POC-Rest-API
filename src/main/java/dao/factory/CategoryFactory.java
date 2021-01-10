package dao.factory;

import entities.Category;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryFactory {

    public static Category getCategoryFromResultSet(ResultSet rs) throws SQLException {
        return new Category(rs.getInt("categoryId".toUpperCase()), rs.getString("categoryName"));
    }

}
