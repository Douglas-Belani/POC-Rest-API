package dao.impl;

import connection.H2Connection;
import dao.IAuxProductCategoryDao;
import dao.IProductDao;
import dao.IRateDao;
import entities.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductDaoImplTest {

    private IProductDao productDao;
    private IRateDao rateDao;
    private IAuxProductCategoryDao auxProductCategoryDao;
    private Connection conn;

    @Before
    public  void initialize() {
        conn = H2Connection.getH2DatabaseConnection();
        productDao = new ProductDaoImpl(conn);
        rateDao = new RateDaoImpl(conn);
        auxProductCategoryDao = new AuxProductCategoryDaoImpl(conn);
    }

    @Test
    public void test_getAllProducts_Should_Return_Products_Id_1_2_3() {

        Product expectedProduct1 = new Product(1, "product1", 50.00,
                "product 1 description", 20, null,
                new Rate(1, 2, 0));
        Product expectedProduct2 = new Product(2, "product2", 50.00,
                "product 2 description", 15, null,
                new Rate(2, 1, 1));
        Product expectedProduct3 = new Product(3, "product3", 20.00,
                "product 3 description", 5, null,
                new Rate(3, 0, 1));

        Category expectedCategory1 = new Category(1, "category1");
        Category expectedCategory2 = new Category(2, "category2");
        Category expectedCategory3 = new Category(3, "category3");
        Category expectedCategory4 = new Category(4, "category4");
        Category expectedCategory5 = new Category(5, "category5");

        expectedProduct1.getCategories().addAll(Arrays.asList(expectedCategory1, expectedCategory2, expectedCategory5));
        expectedProduct2.getCategories().add(expectedCategory3);
        expectedProduct3.getCategories().addAll(Arrays.asList(expectedCategory2, expectedCategory4));

        List<Product> products = productDao.getAllProducts();

        Assert.assertEquals(expectedProduct1, products.get(0));
        Assert.assertEquals(expectedProduct2, products.get(1));
        Assert.assertEquals(expectedProduct3, products.get(2));
        Assert.assertEquals(expectedProduct1.getCategories(), products.get(0).getCategories());
        Assert.assertEquals(expectedProduct2.getCategories(), products.get(1).getCategories());
        Assert.assertEquals(expectedProduct3.getCategories(), products.get(2).getCategories());
    }

    @Test
    public void test_getAllProductsByUser_Should_Return_Products_Id_1_2_For_User_Id_1() {
        User expectedUser1 = new User(1, "user1", "111111111-11",
                "user1@gmail.com", "user1Password",
                LocalDate.of(2005, 8, 10));

        Product expectedProduct1 = new Product(1, "product1", 50.00,
                "product 1 description", 20, null,
                new Rate(1, 2, 0));
        Product expectedProduct2 = new Product(2, "product2", 50.00,
                "product 2 description", 15, null,
                new Rate(2, 1, 1));

        Category expectedCategory1 = new Category(1, "category1");
        Category expectedCategory2 = new Category(2, "category2");
        Category expectedCategory3 = new Category(3, "category3");
        Category expectedCategory5 = new Category(5, "category5");

        expectedProduct1.getCategories().addAll(Arrays.asList(expectedCategory1, expectedCategory2, expectedCategory5));
        expectedProduct2.getCategories().add(expectedCategory3);

        List<Product> products = productDao.getAllProductsByUser(expectedUser1);

        Assert.assertEquals(expectedProduct1, products.get(0));
        Assert.assertEquals(expectedProduct2, products.get(1));
        Assert.assertEquals(expectedProduct1.getCategories(), products.get(0).getCategories());
        Assert.assertEquals(expectedProduct2.getCategories(), products.get(1).getCategories());
    }

    @Test
    public void test_getAllProductsByUser_Should_Return_Empty_List_For_User_Id_4() {
        User expectedUser4 = new User(4, "user4", "444444444-44","user4@gmail.com", "user4Password",
                LocalDate.of(2003, 4, 3));

        List<Product> user4Products = productDao.getAllProductsByUser(expectedUser4);

        Assert.assertEquals(0, user4Products.size());
    }

    @Test
    public void test_getAllProductsByName_Should_Return_Product_Id_1_For_Name_to1_Page_0() {
        String name = "ct1";
        int page = 1;

        Product expectedProduct1 = new Product(1, "product1", 50.00, "product 1 description",
                20, null, new Rate(1, 2 , 0));

        Category expectedCategory1 = new Category(1, "category1");
        Category expectedCategory2 = new Category(2, "category2");
        Category expectedCategory5 = new Category(5, "category5");

        expectedProduct1.getCategories().addAll(Arrays.asList(expectedCategory1, expectedCategory2, expectedCategory5));

        List<Product> products = productDao.getAllProductsByNameAndPage(name, page);

        Assert.assertEquals(1, products.size());
        Assert.assertEquals(expectedProduct1, products.get(0));
        Assert.assertEquals(expectedProduct1.getCategories(), products.get(0).getCategories());
    }

    @Test
    public void test_getAllProductsByName_Should_Return_Product_Id_1_2_3_For_Name_to_Page_0() {
        String name = "ct";
        int page = 1;

        Product expectedProduct1 = new Product(1, "product1", 50.00, "product 1 description",
                20, null, new Rate(1, 2, 0));

        Product expectedProduct2 = new Product(2, "product2", 50.00, "product 2 description",
                15, null, new Rate(2, 1, 1));

        Product expectedProduct3 = new Product(3, "product3", 20.00, "product 3 description",
                5, null, new Rate(3, 0, 1));

        Category expectedCategory1 = new Category(1, "category1");
        Category expectedCategory2 = new Category(2, "category2");
        Category expectedCategory3 = new Category(3, "category3");
        Category expectedCategory4 = new Category(4, "category4");
        Category expectedCategory5 = new Category(5, "category5");

        expectedProduct1.getCategories().addAll(Arrays.asList(expectedCategory1, expectedCategory2, expectedCategory5));
        expectedProduct2.getCategories().add(expectedCategory3);
        expectedProduct3.getCategories().addAll(Arrays.asList(expectedCategory2, expectedCategory4));

        List<Product> products = productDao.getAllProductsByNameAndPage(name, page);

        Assert.assertEquals(3, products.size());
        Assert.assertEquals(expectedProduct1, products.get(0));
        Assert.assertEquals(expectedProduct1.getCategories(), products.get(0).getCategories());
        Assert.assertEquals(expectedProduct2, products.get(1));
        Assert.assertEquals(expectedProduct2.getCategories(), products.get(1).getCategories());
        Assert.assertEquals(expectedProduct3, products.get(2));
        Assert.assertEquals(expectedProduct3.getCategories(), products.get(2).getCategories());
    }

    @Test
    public void test_getAllProductsByName_Should_Return_Empty_List_For_Name_abc() {
        String name = "abc";
        int page = 0;

        List<Product> products = productDao.getAllProductsByNameAndPage(name, page);

        Assert.assertEquals(0, products.size());
    }

    @Test
    public void test_getAllProductsByPage_Should_Return_Products_Id_1_2_For_Page_0_With_Quantity_2() {
        Product expectedProduct1 = new Product(1, "product1", 50.00,
                "product 1 description", 20, null,
                new Rate(1, 2, 0));
        Product expectedProduct2 = new Product(2, "product2", 50.00,
                "product 2 description", 15, null, new Rate(2, 1, 1));
        Product expectedProduct3 = new Product(3, "product3", 20.00, "product 3 description",
                5, null, new Rate(3, 0, 1));

        Category expectedCategory1 = new Category(1, "category1");
        Category expectedCategory2 = new Category(2, "category2");
        Category expectedCategory3 = new Category(3, "category3");
        Category expectedCategory4 = new Category(4, "category4");
        Category expectedCategory5 = new Category(5, "category5");

        expectedProduct1.getCategories().addAll(Arrays.asList(expectedCategory1, expectedCategory2, expectedCategory5));
        expectedProduct2.getCategories().add(expectedCategory3);
        expectedProduct3.getCategories().addAll(Arrays.asList(expectedCategory2, expectedCategory4));

        List<Product> products = productDao.getAllProductsByPage(0, 2);
        Assert.assertEquals(3, products.size());
        Assert.assertEquals(expectedProduct1, products.get(0));
        Assert.assertEquals(expectedProduct1.getCategories(), products.get(0).getCategories());
        Assert.assertEquals(expectedProduct2, products.get(1));
        Assert.assertEquals(expectedProduct2.getCategories(), products.get(1).getCategories());
        Assert.assertEquals(expectedProduct3, products.get(2));
        Assert.assertEquals(expectedProduct3.getCategories(), products.get(2).getCategories());

    }

    @Test
    public void test_getAllProductsByPage_Should_Return_Empty_List() {
        int emptyPage = 15;

        List<Product> products = productDao.getAllProductsByPage(10, emptyPage);

        Assert.assertEquals(0, products.size());
    }

    @Test
    public void test_getAllProductsByPrice_Should_Return_Products_Id_1_2_3_For_Price_50_Page_0() {
        Product expectedProduct1 = new Product(1, "product1", 50.00,
                "product 1 description", 20, null,
                new Rate(1, 2, 0));
        Product expectedProduct2 = new Product(2, "product2", 50.00,
                "product 2 description", 15, null,
                new Rate(2, 1, 1));
        Product expectedProduct3 = new Product(3, "product3", 20.00,
                "product 3 description", 5, null,
                new Rate(3, 0, 1));

        Category expectedCategory1 = new Category(1, "category1");
        Category expectedCategory2 = new Category(2, "category2");
        Category expectedCategory3 = new Category(3, "category3");
        Category expectedCategory4 = new Category(4, "category4");
        Category expectedCategory5 = new Category(5, "category5");

        expectedProduct1.getCategories().addAll
                (Arrays.asList(expectedCategory1, expectedCategory2, expectedCategory5));
        expectedProduct2.getCategories().add(expectedCategory3);
        expectedProduct3.getCategories().addAll(Arrays.asList(expectedCategory2, expectedCategory4));

        List<Product> products = productDao.getAllProductsByPriceAndPage(50.00, 0);

        Assert.assertEquals(3, products.size());
        Assert.assertEquals(expectedProduct1, products.get(0));
        Assert.assertEquals(expectedProduct1.getCategories(), products.get(0).getCategories());
        Assert.assertEquals(expectedProduct2, products.get(1));
        Assert.assertEquals(expectedProduct2.getCategories(), products.get(1).getCategories());
        Assert.assertEquals(expectedProduct3, products.get(2));
        Assert.assertEquals(expectedProduct3.getCategories(), products.get(2).getCategories());

    }

    @Test
    public void test_getAllProductsByPrice_Should_Return_Empty_List_When_There_Is_No_Match() {
        Double price = 10.00;

        List<Product> products = productDao.getAllProductsByPriceAndPage(price, 1);

        Assert.assertEquals(0, products.size());
    }

    @Test
    public void test_getAllProductsByPriceAndCategories_Should_Return_Products_Id_2_For_Category_3_Price_50_Page_0() {
        Product expectedProduct2 = new Product(2, "product2", 50.00,
                "product 2 description", 15, null,
                new Rate(2, 1, 1));

        Category expectedCategory3 = new Category(3, "category3");

        expectedProduct2.getCategories().add(expectedCategory3);

        List<Integer> categoriesIds = new ArrayList<>();
        categoriesIds.add(expectedCategory3.getCategoryId());

        List<Product> products = productDao.
                getAllProductsByPriceAndCategoriesAndPage(50.00, categoriesIds, 1);

        Assert.assertEquals(1, products.size());
        Assert.assertEquals(expectedProduct2, products.get(0));
        Assert.assertEquals(expectedProduct2.getCategories(), products.get(0).getCategories());

    }

    @Test
    public void test_getAllProductsByPriceAndCategories_Should_Return_Empty_List_When_There_Is_No_Match() {
        Category expectedCategory1 = new Category(1, "category1");
        Category expectedCategory3 = new Category(3, "category3");

        List<Integer> categoriesIds = Arrays.asList(
                expectedCategory1.getCategoryId(), expectedCategory3.getCategoryId());

        Double price = 10.00;

        List<Product> products = productDao
                .getAllProductsByPriceAndCategoriesAndPage(price, categoriesIds, 1);

        Assert.assertEquals(0, products.size());
    }

    @Test
    public void test_getAllProductsByCategory_Should_Return_Products_Id_1_3() {
        Product expectedProduct1 = new Product(1, "product1", 50.00,
                "product 1 description", 20, null,
                new Rate(1, 2, 0));
        Product expectedProduct3 = new Product(3, "product3", 20.00,
                "product 3 description", 5, null,
                new Rate(3, 0, 1));

        Category expectedCategory1 = new Category(1, "category1");
        Category expectedCategory2 = new Category(2, "category2");
        Category expectedCategory4 = new Category(4, "category4");
        Category expectedCategory5 = new Category(5, "category5");

        expectedProduct1.getCategories().addAll(Arrays.asList(expectedCategory1, expectedCategory2, expectedCategory5));
        expectedProduct3.getCategories().addAll(Arrays.asList(expectedCategory2, expectedCategory4));

        List<Integer> categoriesIds =
                Arrays.asList(expectedCategory1.getCategoryId(), expectedCategory2.getCategoryId());

        List<Product> products = productDao.getAllProductsByCategoryAndPage(categoriesIds, 1);

        Assert.assertEquals(2, products.size());

        Assert.assertEquals(expectedProduct1, products.get(0));
        Assert.assertEquals(expectedProduct1.getCategories(), products.get(0).getCategories());
        Assert.assertEquals(expectedProduct3, products.get(1));
        Assert.assertEquals(expectedProduct3.getCategories(), products.get(1).getCategories());

    }

    @Test
    public void test_getProductById_Should_Return_Product_Id_1() {
        User expectedUser1 = new User(1, "user1", "111111111-11",
                "user1@gmail.com", "user1Password",
                LocalDate.of(2005, 8, 10));

        Rate expectedRate1 = new Rate(1, 2, 0);

        Product expectedProduct1 = new Product(1, "product1", 50.00,
                "product 1 description", 20, expectedUser1, expectedRate1);

        Category expectedCategory1 = new Category(1, "category1");
        Category expectedCategory2 = new Category(2, "category2");
        Category expectedCategory5 = new Category(5, "category5");

        expectedProduct1.getCategories().addAll(Arrays.asList(expectedCategory1, expectedCategory2, expectedCategory5));

        Product product = productDao.getProductById(1);

        Assert.assertEquals(expectedProduct1, product);
        Assert.assertEquals(expectedProduct1.getCategories(), product.getCategories());
    }

    @Test
    public void test_getProductById_Should_Return_Null_For_Nonexistent_Product_Id() {
        int nonExistentProductId = 10;

        Product product = productDao.getProductById(nonExistentProductId);

        Assert.assertNull(product);
    }

    @Test
    public void test_getProductById_Should_Return_Product_Id_2() {
        User expectedUser1 = new User(1, "user1", "111111111-11",
                "user1@gmail.com", "user1Password", LocalDate.of(2005, 8, 10) );

        Rate expectedRate2 = new Rate(2, 1, 1);

        Product expectedProduct2 = new Product(2, "product2", 50.00,
                "product 2 description", 15, expectedUser1, expectedRate2);

        Category expectedCategory3 = new Category(3, "category3");

        expectedProduct2.getCategories().add(expectedCategory3);

        Product product = productDao.getProductById(2);
        Assert.assertEquals(expectedProduct2, product);
        Assert.assertEquals(expectedProduct2.getCategories(), product.getCategories());

    }

    @Test
    public void test_insertProduct() {
        User expectedUser3 = new User(3, "user3", "333333333-33",
                "user3@gmail.com", "user3Password",
                LocalDate.of(2004, 7, 23));

        Rate rate = new Rate(null, 0, 0);
        Integer rateId = rateDao.insert(rate);
        rate.setRateId(rateId);

        Product newProduct = new Product(null, "product4",
                500.00, "product 4 description", 10,
                expectedUser3, rate);

        Category expectedCategory3 = new Category(3, "category3");
        Category expectedCategory4 = new Category(4, "category4");

        newProduct.getCategories().addAll(Arrays.asList(expectedCategory3, expectedCategory4));

        Integer productId = productDao.insertProduct(newProduct);
        newProduct.setProductId(productId);

        auxProductCategoryDao.insert(productId, expectedCategory3.getCategoryId());
        auxProductCategoryDao.insert(productId, expectedCategory4.getCategoryId());

        Product product = productDao.getProductById(productId);

        Assert.assertEquals(newProduct, product);
        Assert.assertEquals(newProduct.getCategories(), product.getCategories());

    }

    @Test
    public void test_updateProduct_Should_Return_True_On_Successful_Update() {
        Product oldProduct = productDao.getProductById(3);

        oldProduct.setName("product3");
        oldProduct.setPrice(200.00);
        oldProduct.setDescription("product 3 description");
        oldProduct.setStock(5);

        boolean updated = productDao.updateProduct(oldProduct);

        Assert.assertTrue(updated);
    }

    @Test
    public void test_updateProduct_Should_Return_False_For_Nonexistent_Product_Id() {
        Integer nonExistentProductId = 20;

        Product oldProduct = productDao.getProductById(3);
        oldProduct.setProductId(nonExistentProductId);
        oldProduct.setName("produto3");
        oldProduct.setPrice(200.00);
        oldProduct.setDescription("descricao produto 3");
        oldProduct.setStock(5);

        boolean updated = productDao.updateProduct(oldProduct);

        Assert.assertFalse(updated);

    }

    @Test
    public void test_deleteProductById_Should_Return_True_For_Successful_Delete() {
        User user = new User(1, "user1", "111111111-11",
                "user1@gmail.com", "user1Password",
                LocalDate.of(2005, 8, 10));

        Rate rate = new Rate(null, 0, 0);
        Integer rateId = rateDao.insert(rate);
        rate.setRateId(rateId);

        Product product = new Product(null, "produto4", 100.00, "descricao produto 4", 10,
                user, rate);

        Category category = new Category(1, "category 1");
        product.getCategories().add(category);

        Integer productId = productDao.insertProduct(product);
        product.setProductId(productId);

        auxProductCategoryDao.insert(4, 1);
        auxProductCategoryDao.deleteByProductIdAndCategoryId(4, 1);
        boolean deleted = productDao.deleteProduct(product.getProductId());

        Assert.assertTrue(deleted);

    }

    @Test
    public void test_deleteProductById_Should_Return_False_For_Nonexistent_Product_Id() {
        int nonExistentProductId = 35;

        boolean deleted = productDao.deleteProduct(nonExistentProductId);

        Assert.assertFalse(deleted);
    }

    @Test
    public void test_getAllUpvotedProductsByUser_Should_Return_Products_Id_2_3_For_User_Id_1() {
        int userId = 1;

        Product expectedProduct2 = new Product(2, "product2", 50.00,
                "product 2 description", 15, null,
                new Rate(2, 1, 1));
        Product expectedProduct3 = new Product(3, "product3", 20.00,
                "product 3 description", 5, null,
                new Rate(3, 0, 1));

        Category expectedCategory2 = new Category(2, "category2");
        Category expectedCategory3 = new Category(3, "category3");
        Category expectedCategory4 = new Category(4, "category4");

        expectedProduct2.getCategories().add(expectedCategory3);
        expectedProduct3.getCategories().addAll(Arrays.asList(expectedCategory2, expectedCategory4));

        List<Product> upvotedProducts = productDao.getUserUpvotedProducts(userId);

        Assert.assertEquals(2, upvotedProducts.size());
        Assert.assertEquals(expectedProduct2, upvotedProducts.get(0));
        Assert.assertEquals(expectedProduct2.getCategories(), upvotedProducts.get(0).getCategories());
        Assert.assertEquals(expectedProduct3, upvotedProducts.get(1));
        Assert.assertEquals(expectedProduct3.getCategories(), upvotedProducts.get(1).getCategories());

    }

    @Test
    public void test_getAllUpvotedProductsByUser_Should_Return_Empty_List_For_User_Id_4() {
        User expectedUser4 = new User(4, "user4", "444444444-44",
                "user4@gmail.com", "user4Password",
                LocalDate.of(2003, 4, 3));

        List<Product> products = productDao.getUserUpvotedProducts(expectedUser4.getUserId());

        Assert.assertEquals(0, products.size());
    }

}
