package dao.factory;

import entities.Product;
import entities.Rate;
import entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductFactory {

    public static Product getProductFromResultSet(ResultSet rs, User user, Rate rate) throws SQLException {
        return new Product(rs.getInt("productId"), rs.getString("productName"),
                rs.getDouble("price"), rs.getString("description"),
                rs.getInt("stock"), user, rate);
    }

}
