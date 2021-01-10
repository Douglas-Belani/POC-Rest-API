package dao.impl;

import connection.H2Connection;
import dao.IAuxProductCategoryDao;
import dao.IProductDao;
import entities.Category;
import entities.Product;
import entities.Rate;
import entities.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.sql.Connection;
import java.time.LocalDate;
import java.util.Arrays;

public class AUXProductCategoryDaoImplTest {

    private Connection conn;
    private IProductDao productDao;
    private IAuxProductCategoryDao auxProductCategoryDao;

    @Before
    public void initialize() {
        this.conn = H2Connection.getH2DatabaseConnection();
        this.auxProductCategoryDao = new AuxProductCategoryDaoImpl(conn);
        this.productDao = new ProductDaoImpl(conn);
    }

    @Test
    public void test_insert_Should_Return_True_On_Successful_Insert() {
        User expectedUser1 = new User(1, "user1", "111111111-11",
                "user1@gmail.com", "user1Password",
                LocalDate.of(2005, 8, 10) );

        Product expectedProduct1 = new Product(1, "product1", 50.00,
                "product 1 description", 20, expectedUser1,
                new Rate(1, 2, 0));

        Category expectedCategory1 = new Category(1, "category1");
        Category expectedCategory2 = new Category(2, "category2");
        Category expectedCategory4 = new Category(4, "category4");
        Category expectedCategory5 = new Category(5, "category5");

        expectedProduct1.getCategories()
                .addAll(Arrays.asList(expectedCategory1, expectedCategory2, expectedCategory4,expectedCategory5));
        int productId = 1;
        int categoryId = 4;

        boolean inserted = auxProductCategoryDao.insert(productId, categoryId);
        Assert.assertTrue(inserted);

        Product product = productDao.getProductById(productId);
        Assert.assertEquals(expectedProduct1, product);
        Assert.assertEquals(expectedProduct1.getCategories(), product.getCategories());

    }

    @Test
    public void test_updateProduct_Category_Should_Return_True_On_Successful_Update() {
        int oldCategory = 2;
        int newCategory = 3;
        int productId = 1;

        boolean updated = auxProductCategoryDao.update(productId, oldCategory, newCategory);
        Assert.assertTrue(updated);
    }

    @Test
    public void test_updateProductCategory_Should_Return_False() {
        int invalidOldCategory = 4;
        int newCategory = 3;
        int productId = 1;

        boolean updated = auxProductCategoryDao.update(productId, invalidOldCategory, newCategory);

        Assert.assertFalse(updated);
    }

    @Test
    public void test_deleteProductCategory_Should_Return_True_On_Successful_Delete() {
        int productId = 1;
        int categoryId = 5;

        boolean deleted = auxProductCategoryDao.deleteByProductIdAndCategoryId
                (productId, categoryId);

        Assert.assertTrue(deleted);
    }

    @Test
    public void test_deleteProductCategory_Should_Return_False() {
        int productId = 1;
        int categoryId = 4;

        boolean deleted = auxProductCategoryDao
                .deleteByProductIdAndCategoryId(productId, categoryId);

        Assert.assertFalse(deleted);
    }
}
