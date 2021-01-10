package services;

import entities.Product;
import entities.User;

import java.sql.SQLException;
import java.util.List;

public interface IProductService {

    public abstract List<Product> getAllProducts();

    public abstract List<Product> getUserUpvotedProducts(User user);

    public abstract Product getProductById(Integer id);

    public abstract List<Product> getAllProductsByUser(User user);

    public abstract List<Product> searchProductsByNameAndPage(String name, int page);

    public abstract List<Product> searchProductsByPriceAndPage(Double price, int page);

    public abstract List<Product> searchProductsByCategoriesAndPage(List<Integer> categoriesIds, int page);

    public abstract List<Product> searchProductsByPriceAndCategoriesAndPage(List<Integer> categoriesIds,
                                                                            Double price, int page);

    public abstract List<Product> searchProductsByPage(int page);

    public abstract Integer listProduct(User user, Product product) throws SQLException;

    public abstract boolean editProduct(User user, Product product, Integer productId) throws SQLException;

}
