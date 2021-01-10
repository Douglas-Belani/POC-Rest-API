package services.impl;

import dao.IAuxProductCategoryDao;
import dao.ICommentDao;
import dao.IProductDao;
import dao.impl.AuxProductCategoryDaoImpl;
import dao.impl.CommentDaoImpl;
import dao.impl.ProductDaoImpl;
import entities.*;
import resources.exception.ResourceNotFoundException;
import services.IEmailService;
import services.IProductService;
import services.IRateService;
import services.exceptions.UnauthorizedException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ProductService implements IProductService {

    private Connection conn;
    private IProductDao productDao;
    private IAuxProductCategoryDao auxProductCategory;
    private IRateService rateService;
    private ICommentDao commentDao;
    private IEmailService emailService;
    private static final int QUANTITY = 10;

    public ProductService(Connection conn) {
        this(new ProductDaoImpl(conn), new RateServiceImpl(conn), new AuxProductCategoryDaoImpl(conn),
                new CommentDaoImpl(conn));
        this.conn = conn;
    }

    public ProductService(IProductDao productDao, IRateService rateService,
                          IAuxProductCategoryDao auxProductCategory, ICommentDao commentDao) {
        this.productDao = productDao;
        this.rateService = rateService;
        this.auxProductCategory = auxProductCategory;
        this.commentDao = commentDao;
    }

    public ProductService(Connection conn, IProductDao productDao, IRateService rateService,
                          IAuxProductCategoryDao auxProductCategory, ICommentDao commentDao) {
        this.conn = conn;
        this.productDao = productDao;
        this.rateService = rateService;
        this.auxProductCategory = auxProductCategory;
        this.commentDao = commentDao;
    }

    @Override
    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    @Override
    public List<Product> getUserUpvotedProducts(User user) {
        return productDao.getUserUpvotedProducts(user.getUserId());
    }

    @Override
    public Product getProductById(Integer id) {
        Product product = productDao.getProductById(id);

        if (product == null) {
            throw new ResourceNotFoundException("Product id " + id + " not found");
        }

        List<Comments> comments = commentDao.getAllCommentsByProductId(product.getProductId());
        product.setComment(comments);
        return product;
    }

    @Override
    public List<Product> getAllProductsByUser(User user) {
        return productDao.getAllProductsByUser(user);
    }

    @Override
    public List<Product> searchProductsByNameAndPage(String name, int page) {
        if (name == null || name.isBlank()) {
            return searchProductsByPage(page);
        } else {
            if (page < 1) {
                page = 1;
            }
            return productDao.getAllProductsByNameAndPage(name, page);
        }
    }

    @Override
    public List<Product> searchProductsByPriceAndPage(Double price, int page) {
        if (price == null || price < 0) {
            return searchProductsByPage(page);

        } else {
            if (page < 1) {
                page = 1;
            }
            return productDao.getAllProductsByPriceAndPage(price, page);
        }
    }

    @Override
    public List<Product> searchProductsByCategoriesAndPage(List<Integer> categoriesIds, int page) {
        if (categoriesIds == null) {
            return searchProductsByPage(page);

        }
        categoriesIds.removeIf(categorieId -> categorieId < 1);

        if (categoriesIds.isEmpty()) {
            return searchProductsByPage(page);
        }

        if (page < 1) {
            page = 1;
        }

        return productDao.getAllProductsByCategoryAndPage(categoriesIds, page);
    }

    @Override
    public List<Product> searchProductsByPriceAndCategoriesAndPage(List<Integer> categoriesIds,
                                                                   Double price, int page) {

        if (categoriesIds == null) {
            return searchProductsByPriceAndPage(price, page);
        }

        categoriesIds.removeIf(categorieId -> categorieId < 1);
        if (page < 1) {
            page = 1;
        }

        if (categoriesIds.isEmpty()) {
            return searchProductsByPriceAndPage(price, page);
        } else if (price == null || price < 0) {
            return searchProductsByCategoriesAndPage(categoriesIds, page);
        } else {
            return productDao.getAllProductsByPriceAndCategoriesAndPage(price, categoriesIds, page);
        }
    }

    @Override
    public List<Product> searchProductsByPage(int page) {
        if (page < 1) {
            return productDao.getAllProductsByPage(1, QUANTITY);

        } else {
            return productDao.getAllProductsByPage(page, QUANTITY);
        }
    }

    @Override
    public Integer listProduct(User user, Product product) throws SQLException {
        product.getCategories().removeIf(category -> category.getCategoryId() < 1);

        if (product.getCategories().isEmpty()) {
            throw new IllegalArgumentException("Product must have at least 1 category.");
        }
        try {
            conn.setAutoCommit(false);
            Rate rate = rateService.createRate();

            if (rate.getRateId() == null) {
                conn.rollback();
                return null;
            }

            product.setRate(rate);
            product.setUser(user);

            Integer inserted = productDao.insertProduct(product);

            if (inserted == null) {
                conn.rollback();
                return null;
            }

            for (Category category : product.getCategories()) {
                boolean insertedCategory = auxProductCategory.insert(inserted, category.getCategoryId());
                if (!insertedCategory) {
                    conn.rollback();
                    return null;
                }
            }
            conn.commit();

            Thread thread = new Thread(() -> {emailService = new EmailServiceImpl();
                emailService.sendEmail(user.getEmail(), "Product successfully registered",
                        product.toString());});
            thread.start();

            return inserted;

        } catch (SQLException e) {
            conn.rollback();
            return null;

        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public boolean editProduct(User user, Product product, Integer productId) throws SQLException {
        try {
            conn.setAutoCommit(false);
            Product persistedProduct = productDao.getProductById(productId);

            if (persistedProduct == null) {
                throw new ResourceNotFoundException("Product id " + productId + " not found");
            }
            if (!persistedProduct.getUser().equals(user)) {
                throw new UnauthorizedException("Can't edit another user's product");
            }

            List<Category> commonCategories =
                    persistedProduct.getCategories().stream()
                            .filter(category -> product.getCategories().contains(category))
                            .collect(Collectors.toList());

            persistedProduct.getCategories()
                    .removeIf(category -> product.getCategories().contains(category));
            persistedProduct.getCategories().forEach(category ->
                    auxProductCategory.deleteByProductIdAndCategoryId(productId, category.getCategoryId()));

            product.getCategories().removeIf(category -> commonCategories.contains(category));
            product.getCategories().forEach(category ->
                    auxProductCategory.insert(productId, category.getCategoryId()));

            product.setProductId(productId);
            boolean updated = productDao.updateProduct(product);
            conn.commit();
            return updated;

        } catch (SQLException e) {
            conn.rollback();
            return false;

        } finally {
            conn.setAutoCommit(true);
        }
    }
}
