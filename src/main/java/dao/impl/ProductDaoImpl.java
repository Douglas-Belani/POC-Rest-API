package dao.impl;

import dao.IProductDao;
import dao.connection.DatabaseConnection;
import dao.factory.CategoryFactory;
import dao.factory.ProductFactory;
import dao.factory.RateFactory;
import dao.factory.UserFactory;
import entities.Category;
import entities.Product;
import entities.Rate;
import entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements IProductDao {

    private Connection conn;

    public ProductDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Product> getAllProducts() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Product> products  = new ArrayList<>();

        try {
            String query = "SELECT `p`.productId, `p`.productName, `p`.price, `p`.description, `p`.stock, " +
                           "`ca`.categoryId, `ca`.categoryName, " +
                           "`r`.rateId, `r`.upvotes, `r`.downvotes " +
                           "FROM product `p` " +
                           "INNER JOIN  aux_product_category `apc` ON `p`.productId = `apc`.productId " +
                           "INNER JOIN category `ca` ON `apc`.categoryId = `ca`.categoryId " +
                           "INNER JOIN rate `r` ON `p`.rateId = `r`.rateId " +
                           "ORDER BY `p`.productId";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();

            if (rs.next()) {
                while (!rs.isAfterLast()) {
                    Rate rate = RateFactory.getRateFromResultSetRs(rs);
                    Product product = ProductFactory.getProductFromResultSet(rs, null, rate);

                    while(!rs.isAfterLast() && rs.getInt("productId") == product.getProductId()) {
                        Category category = CategoryFactory.getCategoryFromResultSet(rs);
                        product.addCategory(category);
                        rs.next();
                    }

                    products.add(product);
                }
            }

            return products;

        } catch (SQLException e) {
            e.printStackTrace();
            return products;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public List<Product> getAllProductsByUser(User user) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Product> products = new ArrayList<>();

        try {
           String query = "SELECT `p`.productId, `p`.productName, `p`.price, `p`.description, `p`.stock, " +
                          "`ca`.categoryId, `ca`.categoryName, " +
                          "`r`.rateId, `r`.upvotes, `r`.downvotes " +
                          "FROM product `p` " +
                          "INNER JOIN aux_product_category `apc` ON `p`.productId = `apc`.productId " +
                          "INNER JOIN category `ca` ON `apc`.categoryId = `ca`.categoryId " +
                          "INNER JOIN rate `r` ON `p`.productId = `r`.rateId " +
                          "WHERE `p`.userId = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, user.getUserId());
            rs = ps.executeQuery();

            if (rs.next()) {
                while (!rs.isAfterLast()) {
                    Rate rate = RateFactory.getRateFromResultSetRs(rs);
                    Product product = ProductFactory.getProductFromResultSet(rs, null, rate);

                    while (!rs.isAfterLast() && rs.getInt("productId") == product.getProductId()) {
                        Category category = CategoryFactory.getCategoryFromResultSet(rs);
                        product.addCategory(category);
                        rs.next();
                    }

                    products.add(product);
                }
            }

            return products;

        } catch (SQLException e) {
            e.printStackTrace();
            return products;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public List<Product> getUserUpvotedProducts(int userId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Product> products = new ArrayList<>();

        try {
            String sql = "SELECT `p`.productId, `p`.productName, `p`.price, `p`.description, `p`.stock, " +
                         "`ca`.categoryId, `ca`.categoryName, " +
                         "`r`.rateId, `r`.upvotes, `r`.downvotes " +
                         "FROM product `p` " +
                         "INNER JOIN aux_product_category `apc` ON `p`.productId = `apc`.productId " +
                         "INNER JOIN category `ca` ON `apc`.categoryId = `ca`.categoryId " +
                         "INNER JOIN rate `r` ON `p`.rateId = `r`.rateId " +
                         "INNER JOIN aux_rate_user `aru` ON `r`.rateId = `aru`.rateId " +
                         "WHERE `aru`.userId = ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            if (rs.next()) {
                while (!rs.isAfterLast()) {
                    Rate rate = RateFactory.getRateFromResultSetRs(rs);
                    Product product = ProductFactory.getProductFromResultSet(rs, null, rate);

                    while (!rs.isAfterLast() && rs.getInt("productId") == product.getProductId()) {
                        Category category = CategoryFactory.getCategoryFromResultSet(rs);
                        product.addCategory(category);
                        rs.next();
                    }

                    products.add(product);
                }
            }

            return products;

        } catch (SQLException e) {
            e.printStackTrace();
            return products;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public Product getProductById(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Product product = null;

        try {
            String query = "SELECT `p`.productId, `p`.productName, `p`.price, `p`.description, `p`.stock, " +
                           "`ca`.categoryId, `ca`.categoryName, " +
                           "`r`.rateId, `r`.upvotes, `r`.downvotes, " +
                           "`u`.userId, `u`.userName, `u`.cpf,`u`.email, `u`.password, `u`.birthDate " +
                           "FROM product `p` " +
                           "INNER JOIN aux_product_category `apc` ON `p`.productId = `apc`.productId " +
                           "INNER JOIN category `ca` ON `apc`.categoryId = `ca`.categoryId " +
                           "INNER JOIN rate `r` ON `p`.rateId = `r`.rateId " +
                           "INNER JOIN user `u` ON `p`.userId = `u`.userId " +
                           "WHERE `p`.productId = ?";

            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();


            if (rs.next()) {
                User user = UserFactory.getUserFromResultSet(rs);
                Rate rate = RateFactory.getRateFromResultSetRs(rs);
                product = ProductFactory.getProductFromResultSet(rs, user, rate);

                while (!rs.isAfterLast()) {
                    Category category = CategoryFactory.getCategoryFromResultSet(rs);
                    product.addCategory(category);
                    rs.next();
                }
            }

            return product;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public List<Product> getAllProductsByNameAndPage(String name, int page) {
       PreparedStatement ps = null;
       ResultSet rs = null;
       List<Product> products = new ArrayList<>();

       try {
           String query = "SELECT `p`.productId, `p`.productName, `p`.price, `p`.description, `p`.stock, " +
                          "`ca`.categoryId, `ca`.categoryName, " +
                          "`r`.rateId, `r`.upvotes, `r`.downvotes " +
                          "FROM (SELECT productId, productName, price, description, stock, rateId  " +
                                "FROM product " +
                                "WHERE productName LIKE ? " +
                                "LIMIT ?, 10) AS `p` " +
                          "INNER JOIN aux_product_category `apc` ON `p`.productId = `apc`.productId " +
                          "INNER JOIN category `ca` ON `apc`.categoryId = `ca`.categoryId " +
                          "INNER JOIN rate `r` ON `p`.rateId = `r`.rateId " +
                          "ORDER BY `p`.productId ";
           ps = conn.prepareStatement(query);
           ps.setString(1, "%" + name + "%");
           ps.setInt(2, (page - 1) * 10);
           rs = ps.executeQuery();

           if (rs.next()) {
               while (!rs.isAfterLast()) {
                   Rate rate = RateFactory.getRateFromResultSetRs(rs);
                   Product product = ProductFactory.getProductFromResultSet(rs, null, rate);

                   while (!rs.isAfterLast() && rs.getInt("productId") == product.getProductId()) {
                       Category category = CategoryFactory.getCategoryFromResultSet(rs);
                       product.addCategory(category);
                       rs.next();
                   }

                   products.add(product);
               }
           }


           return products;

       } catch (SQLException e) {
           e.printStackTrace();
           return products;

       } finally {
           DatabaseConnection.closeResultSet(rs);
           DatabaseConnection.closePreparedStatement(ps);
       }
    }

    @Override
    public List<Product> getAllProductsByCategoryAndPage(List<Integer> categoriesIds, int page) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Product> products = new ArrayList<>();

        try {
            StringBuilder filter = new StringBuilder("IN (");
            for (Integer i : categoriesIds) {
                filter.append(i).append(",");
            }

            filter.replace(filter.length() -1 , filter.length(), ")");


            String sql = "SELECT DISTINCT `p`.productId, `p`.productName, `p`.price, `p`.description, `p`.stock,  " +
                         "`ca`.categoryId, `ca`.categoryName, " +
                         "`r`.rateId, `r`.upvotes, `r`.downvotes " +
                         "FROM (SELECT `pr`.productId, productName, price, description, stock, rateId " +
                         "FROM product `pr` " +
                         "INNER JOIN aux_product_category `apc` ON `pr`.productId = `apc`.productId " +
                         "WHERE `apc`.categoryId " + filter + " " +
                         "LIMIT ?, 10) AS `p` " +
                         "INNER JOIN aux_product_category `apc` ON `p`.productId = `apc`.productId " +
                         "INNER JOIN category `ca` ON `apc`.categoryId = `ca`.categoryId " +
                         "INNER JOIN rate `r` ON `p`.rateId = `r`.rateId " +
                         "ORDER BY `p`.productId ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, (page - 1) * 10);
            rs = ps.executeQuery();

            if (rs.next()) {
                while (!rs.isAfterLast()) {
                    Rate rate = RateFactory.getRateFromResultSetRs(rs);
                    Product product = ProductFactory.getProductFromResultSet(rs, null, rate);

                    while (!rs.isAfterLast() && rs.getInt("productId") == product.getProductId()) {
                        Category category = CategoryFactory.getCategoryFromResultSet(rs);
                        product.addCategory(category);
                        rs.next();
                    }

                    products.add(product);
                }
            }

            return products;

        } catch (SQLException e) {
            e.printStackTrace();
            return products;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public List<Product> getAllProductsByPriceAndPage(Double price, int page) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Product> products = new ArrayList<>();

        try {
            String sql = "SELECT `p`.productId, `p`.productName, `p`.price, `p`.description, `p`.stock, " +
                         "`ca`.categoryId, `ca`.categoryName, " +
                         "`r`.rateId, `r`.upvotes, `r`.downvotes " +
                         "FROM (SELECT productId, productName, price, description, stock, rateId " +
                         "FROM product " +
                         "WHERE price <= ? " +
                         "LIMIT ?, 10) AS `p`" +
                         "INNER JOIN aux_product_category `apc` ON `p`.productId = `apc`.productId " +
                         "INNER JOIN category `ca` ON `apc`.categoryId = `ca`.categoryId " +
                         "INNER JOIN rate `r` ON `p`.rateId = `r`.rateId " +
                         "ORDER BY `p`.productId ";
            ps = conn.prepareStatement(sql);
            ps.setDouble(1, price);
            ps.setInt(2, (page - 1) * 10);
            rs = ps.executeQuery();

            if (rs.next()) {
                while (!rs.isAfterLast()) {
                    Rate rate = RateFactory.getRateFromResultSetRs(rs);
                    Product product = ProductFactory.getProductFromResultSet(rs, null, rate);

                    while (!rs.isAfterLast() && rs.getInt("productId") == product.getProductId()) {
                        Category category = CategoryFactory.getCategoryFromResultSet(rs);
                        product.addCategory(category);
                        rs.next();
                    }

                    products.add(product);
                }
            }

            return products;

        } catch (SQLException e) {
            e.printStackTrace();
            return products;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public List<Product> getAllProductsByPriceAndCategoriesAndPage(Double price, List<Integer> categoriesIds, int page) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Product> products = new ArrayList<>();

        try {
            StringBuilder filter = new StringBuilder("IN (");
            for(Integer i : categoriesIds) {
                filter.append(i).append(",");
            }

            filter.replace(filter.length() -1, filter.length(), ")");

            String sql = "SELECT `p`.productId, `p`.productName, `p`.price, `p`.description, `p`.stock, " +
                         "`ca`.categoryId, `ca`.categoryName, " +
                         "`r`.rateId, `r`.upvotes, `r`.downvotes " +
                         "FROM (SELECT `pr`.productId, productName, price, description, stock, rateId " +
                         "FROM product `pr` " +
                         "INNER JOIN aux_product_category `apc` ON `pr`.productId = `apc`.productId " +
                         "WHERE price <= ? AND categoryId " + filter + " " +
                         "LIMIT ?, 10 ) AS `p` " +
                         "INNER JOIN aux_product_category `apc` ON `p`.productId = `apc`.productId " +
                         "INNER JOIN category `ca` ON `apc`.categoryId = `ca`.categoryId   " +
                         "INNER JOIN rate `r` ON `p`.rateId = `r`.rateId " +
                         "ORDER BY `p`.productId ";
            ps = conn.prepareStatement(sql);
            ps.setDouble(1, price);
            ps.setInt(2, (page - 1) * 10);
            rs = ps.executeQuery();

            if (rs.next()) {
                while (!rs.isAfterLast()) {
                    Rate rate = RateFactory.getRateFromResultSetRs(rs);
                    Product product = ProductFactory.getProductFromResultSet(rs, null, rate);

                    while (!rs.isAfterLast() && rs.getInt("productId") == product.getProductId()) {
                        Category category = CategoryFactory.getCategoryFromResultSet(rs);
                        product.addCategory(category);
                        rs.next();
                    }

                    products.add(product);
                }
            }

            return products;

        } catch (SQLException e) {
            e.printStackTrace();
            return products;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public List<Product> getAllProductsByPage(int page, int quantity) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Product> products = new ArrayList<>();

        try {
            String sql = "SELECT `p`.productId, `p`.productName, `p`.price, `p`.description, `p`.stock, " +
                         "`ca`.categoryId, `ca`.categoryName, " +
                         "`r`.rateId, `r`.upvotes, `r`.downvotes " +
                         "FROM (SELECT productId, productName, price, description, stock, rateId " +
                         "FROM product " +
                         "LIMIT ?, 10 ) AS `p` " +
                         "INNER JOIN aux_product_category `apc` ON `p`.productId = `apc`.productId " +
                         "INNER JOIN category `ca` ON `apc`.categoryId = `ca`.categoryId " +
                         "INNER JOIN rate `r` ON `p`.rateId = `r`.rateId " +
                         "ORDER BY `p`.productId ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, (page - 1) * quantity);
            rs = ps.executeQuery();

            if (rs.next()) {
                while (!rs.isAfterLast()) {
                    Rate rate = RateFactory.getRateFromResultSetRs(rs);
                    Product product = ProductFactory.getProductFromResultSet(rs, null, rate);

                    while (!rs.isAfterLast() && rs.getInt("productId") == product.getProductId()) {
                        Category category = CategoryFactory.getCategoryFromResultSet(rs);
                        product.addCategory(category);
                        rs.next();
                    }

                    products.add(product);
                }
            }

            return products;

        } catch (SQLException e) {
            e.printStackTrace();
            return products;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closePreparedStatement(ps);
        }
    }

    @Override
    public Integer insertProduct(Product product) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer id = null;

        try {
            String sqlCommand = "INSERT INTO product VALUES(NULL, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setString(3, product.getDescription());
            ps.setInt(4, product.getStock());
            ps.setInt(5, product.getUser().getUserId());
            ps.setInt(6, product.getRate().getRateId());
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
    public boolean updateProduct(Product product) {
        PreparedStatement ps = null;

        try {
            String sqlCommand = "UPDATE product " +
                                "SET productName = ?, price = ?, description = ?, stock = ? " +
                                "WHERE productId = ? ";
            ps = conn.prepareStatement(sqlCommand);
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setString(3, product.getDescription());
            ps.setInt(4, product.getStock());
            ps.setInt(5, product.getProductId());

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
    public boolean deleteProduct(int id) {
        PreparedStatement ps = null;

        try {
            String sqlCommand = "DELETE FROM product WHERE productId = ?";
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
