package dao;

import entities.Product;
import entities.User;

import java.util.List;

public interface IProductDao {

    public abstract List<Product> getAllProducts();

    public abstract List<Product> getAllProductsByUser(User user);

    public abstract List<Product> getUserUpvotedProducts(int userId);

    public abstract Product getProductById(int id);

    public abstract List<Product> getAllProductsByNameAndPage(String name, int page);

    public abstract List<Product> getAllProductsByCategoryAndPage(List<Integer> categoriesIds, int page);

    public abstract List<Product> getAllProductsByPriceAndPage(Double price, int page);

    public abstract List<Product> getAllProductsByPriceAndCategoriesAndPage(Double price,
                                                                     List<Integer> categoriesIds, int page);

    public abstract List<Product> getAllProductsByPage(int page, int quantity);

    public abstract Integer insertProduct(Product product);

    public abstract boolean updateProduct(Product product);

    public abstract boolean deleteProduct(int id);

}
