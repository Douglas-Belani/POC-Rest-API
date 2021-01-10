package services.impl;

import dao.IAuxProductCategoryDao;
import dao.ICommentDao;
import dao.IProductDao;
import entities.Category;
import entities.Product;
import entities.Rate;
import entities.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import resources.exception.ResourceNotFoundException;
import services.IProductService;
import services.IRateService;
import services.exceptions.UnauthorizedException;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    Connection conn;

    @Mock
    IProductDao productDao;

    @Mock
    IRateService rateService;

    @Mock
    IAuxProductCategoryDao auxProductCategoryDao;

    @Mock
    ICommentDao commentDao;

    private IProductService productService;

    @Before
    public void initialize() {
        productService = new ProductService(conn, productDao, rateService, auxProductCategoryDao, commentDao);
    }

    @Test
    public void test_searchProductsByName_Should_Return_SearchProductsByPage_If_Name_Is_Null() {
        String name = null;
        int page = 1;

        ProductService mocked = Mockito.mock(ProductService.class);
        Mockito.doCallRealMethod().when(mocked).searchProductsByNameAndPage(name, page);
        mocked.searchProductsByNameAndPage(name, page);
        Mockito.verify(mocked, Mockito.times(1)).searchProductsByPage(page);
    }

    @Test
    public void test_searchProductsByName_Should_Return_Search_ProductsByPage_If_Name_Is_Blank() {
        String name = "";
        int page = 1;

        ProductService mocked = Mockito.mock(ProductService.class);
        Mockito.doCallRealMethod().when(mocked).searchProductsByNameAndPage(name, page);
        mocked.searchProductsByNameAndPage(name, page);
        Mockito.verify(mocked, Mockito.times(1)).searchProductsByPage(page);
    }

    @Test
    public void test_searchProductByName_Should_Set_Page_To_1_If_Page_Is_Less_Than_1_And_Return_ProductDao_GetAllProductsByNameAndPage () {
        String name = "name";
        int page = -1;

        productService.searchProductsByNameAndPage(name, page);
        Mockito.verify(productDao, Mockito.times(1)).getAllProductsByNameAndPage(name, 1);
    }

    @Test
    public void test_searchProductByName_Should_Return_ProductDao_GetAllProductsByNameAndPage_If_Name_Is_Not_Blank_Or_Null() {
        String name = "name";
        int page = 1;

        productService.searchProductsByNameAndPage(name, page);
        Mockito.verify(productDao, Mockito.times(1)).getAllProductsByNameAndPage(name, page);
    }

    @Test
    public void test_searchProductsByPriceAndPage_Should_Return_searchProductsByPage_For_Null_Price() {
        Double price = null;
        int page = 2;

        ProductService mock = Mockito.mock(ProductService.class);
        Mockito.doCallRealMethod().when(mock).searchProductsByPriceAndPage(price, page);
        mock.searchProductsByPriceAndPage(price, page);
        Mockito.verify(mock, Mockito.times(1)).searchProductsByPriceAndPage(price, page);
    }

    @Test
    public void test_searchProductByPriceAndPage_Should_Return_searchProductByPage_For_Negative_Price() {
        Double price = -1.0;
        int page = 2;

        ProductService mock = Mockito.mock(ProductService.class);
        Mockito.doCallRealMethod().when(mock).searchProductsByPriceAndPage(price, page);
        mock.searchProductsByPriceAndPage(price, page);
        Mockito.verify(mock, Mockito.times(1)).searchProductsByPriceAndPage(price, page);
    }

    @Test
    public void test_searchProductByPriceAndPage_Should_Return_ProductDao_getAllProductsByPriceAndPage_With_Page_1_When_Page_Smaller_Than_1_Is_Passed() {
        Double price = 100.0;
        int page = -1;

        productService.searchProductsByPriceAndPage(price, page);
        Mockito.verify(productDao, Mockito.times(1)).getAllProductsByPriceAndPage(price, 1);
    }

    @Test
    public void test_searchProductByPriceAndPage_Should_Return_ProductDao_getAllProductsByPriceAndPage_For_Non_Null_And_Negative_Price() {
        Double price = 50.00;
        int page = 1;

        productService.searchProductsByPriceAndPage(price, page);
        Mockito.verify(productDao, Mockito.times(1)).getAllProductsByPriceAndPage(price, page);
    }

    @Test
    public void test_searchProductsByCategoriesAndPage_Should_Return_searchProductsByPage_For_Null_Category_Id_List() {
        int page = 1;
        List<Integer> categoryId = null;

        ProductService mock = Mockito.mock(ProductService.class);
        Mockito.doCallRealMethod().when(mock).searchProductsByCategoriesAndPage(categoryId, page);
        mock.searchProductsByCategoriesAndPage(categoryId, page);
        Mockito.verify(mock, Mockito.times(1)).searchProductsByPage(page);
    }

    @Test
    public void test_searchProductsByCategoriesAndPage_Should_Return_searchProductsByPage_For_Empty_Category_Id_List() {
        int page = 1;
        List<Integer> categoryId = new ArrayList<>();

        ProductService mock = Mockito.mock(ProductService.class);
        Mockito.doCallRealMethod().when(mock).searchProductsByCategoriesAndPage(categoryId, page);
        mock.searchProductsByCategoriesAndPage(categoryId, page);
        Mockito.verify(mock, Mockito.times(1)).searchProductsByPage(page);
    }

    @Test
    public void test_searchProductsByCategoriesAndPage_Should_Return_searchProductsByPage_If_All_Id_In_Category_Id_List_Is_Less_Than_1() {
        int page = 1;
        List<Integer> categoryId = new ArrayList<>();
        categoryId.addAll(Arrays.asList(-3, -2, -1, 0));

        ProductService mock = Mockito.mock(ProductService.class);
        Mockito.doCallRealMethod().when(mock).searchProductsByCategoriesAndPage(categoryId, page);
        mock.searchProductsByCategoriesAndPage(categoryId, page);
        Mockito.verify(mock, Mockito.times(1)).searchProductsByPage(page);
    }

    @Test
    public void test_searchProductsByCategoriesAndPage_Should_Return_ProductDao_GetAllProductsByCategoryAndPage_With_Page_1_When_Page_Smaller_Than_1_Is_Passed() {
        int page = -1;
        List<Integer> categoryId = Arrays.asList(1, 2, 3);

        productService.searchProductsByCategoriesAndPage(categoryId, page);
        Mockito.verify(productDao, Mockito.times(1)).getAllProductsByCategoryAndPage(categoryId,
                1);

    }

    @Test
    public void test_searchProductsByCategoriesAndPage_Should_Return_ProductDao_GetAllProductsByCategoryAndPage_For_Non_Null_And_Empty_CategoryId_List() {
        int page = 1;
        List<Integer> categoryId = Arrays.asList(1, 2, 3);
        productService.searchProductsByCategoriesAndPage(categoryId, page);
        Mockito.verify(productDao, Mockito.times(1)).getAllProductsByCategoryAndPage(categoryId,
                page);
    }

    @Test
    public void test_searchProductsByPriceAndCategoriesAndPage_Should_Return_searchProductsByPriceAndPage_For_Null_Category_Id_List() {
        int page = 1;
        List<Integer> categoryId = null;
        Double price = 50.00;

        ProductService mock = Mockito.mock(ProductService.class);
        Mockito.doCallRealMethod().when(mock).searchProductsByPriceAndCategoriesAndPage(categoryId, price, page);
        mock.searchProductsByPriceAndCategoriesAndPage(categoryId, price, page);
        Mockito.verify(mock, Mockito.times(1)).searchProductsByPriceAndPage(price, page);
    }

    @Test
    public void test_searchProductsByPriceAndCategoriesAndPage_Should_Return_searchProductsByPriceAndPage_If_All_Ids_In_Category_Id_List_Is_Less_Than_1() {
        int page = 1;
        List<Integer> categoryId = new ArrayList<>();
        categoryId.addAll(Arrays.asList(-3, -2));
        Double price = 50.00;

        ProductService mock = Mockito.mock(ProductService.class);
        Mockito.doCallRealMethod().when(mock).searchProductsByPriceAndCategoriesAndPage(categoryId, price, page);
        mock.searchProductsByPriceAndCategoriesAndPage(categoryId, price, page);
        Mockito.verify(mock, Mockito.times(1)).searchProductsByPriceAndPage(price, page);
    }

    @Test
    public void test_searchProductsByPriceAndCategoriesAndPage_Should_Return_searchProductsByPriceAndPage_If_Category_Id_List_Is_Empty() {
        int page = 1;
        List<Integer> categoryId = new ArrayList<>();
        Double price = 50.00;

        ProductService mock = Mockito.mock(ProductService.class);
        Mockito.doCallRealMethod().when(mock).searchProductsByPriceAndCategoriesAndPage(categoryId, price, page);
        mock.searchProductsByPriceAndCategoriesAndPage(categoryId, price, page);
        Mockito.verify(mock, Mockito.times(1)).searchProductsByPriceAndPage(price, page);
    }

    @Test
    public void test_searchProductsByPriceAndCategories_Should_Return_searchProductsByCategoriesAndPage_If_Categories_Id_List_Is_Not_Null_Or_Empty_And_Price_Is_Null() {
        int page = 1;
        List<Integer> categoryId = new ArrayList<>();
        categoryId.addAll(Arrays.asList(1, 2, 3));
        Double price = null;

        ProductService mock = Mockito.mock(ProductService.class);
        Mockito.doCallRealMethod().when(mock).searchProductsByPriceAndCategoriesAndPage(categoryId, price, page);
        mock.searchProductsByPriceAndCategoriesAndPage(categoryId, price, page);
        Mockito.verify(mock, Mockito.times(1)).searchProductsByCategoriesAndPage(categoryId, page);
    }

    @Test
    public void test_searchProductsByPriceAndCategories_Should_Return_searchProductsByCategoriesAndPage_If_CategoryId_List_Is_Not_Null_Or_Empty_And_Price_Is_Less_Than_0() {
        int page = 1;
        List<Integer> categoryId = new ArrayList<>();
        categoryId.addAll(Arrays.asList(1, 2, 3));
        Double price = -1.0;

        ProductService mock = Mockito.mock(ProductService.class);
        Mockito.doCallRealMethod().when(mock).searchProductsByPriceAndCategoriesAndPage(categoryId, price, page);
        mock.searchProductsByPriceAndCategoriesAndPage(categoryId, price, page);
        Mockito.verify(mock, Mockito.times(1)).searchProductsByCategoriesAndPage(categoryId, page);
    }

    @Test
    public void test_searchProductsByPriceAndCategories_Should_Return_ProductDao_getAllProductsByPriceAndCategoriesAndPage_If_CategoryId_List_Is_Not_Null_Or_Empty_And_Price_Is_Not_Null_Or_Less_Than_0() {
        int page = 1;
        List<Integer> categoryId = new ArrayList<>();
        categoryId.addAll(Arrays.asList(1, 2, 3));
        Double price = 10.0;

        productService.searchProductsByPriceAndCategoriesAndPage(categoryId, price, page);
        Mockito.verify(productDao, Mockito.times(1)).getAllProductsByPriceAndCategoriesAndPage(price, categoryId, page);
    }

    @Test
    public void test_searchProductsByPage_Should_Return_Product_Dao_getAllProducts_By_Page_With_Page_1_If_Passed_Page_Is_Less_Than_1() {
        int page = -1;

        productService.searchProductsByPage(page);
        Mockito.verify(productDao, Mockito.times(1)).getAllProductsByPage(1, 10);
    }

    @Test
    public void test_searchProductByPage_Should_Return_Product_Dao_getAllProducts_By_Page() {
        int page = 1;

        productService.searchProductsByPage(page);
        Mockito.verify(productDao, Mockito.times(1)).getAllProductsByPage(1, 10);
    }

    @Test
    public void test_listProduct_Should_Throw_IllegalArgumentException_If_Product_CategoryId_List_Is_Empty() {
        User user1 = new User(1, "fullName", "111111111-11", "email1@gmail.com",
                "P@ssword1", LocalDate.now());
        Product product = new Product(1, "name", 100.00, "description",
                10, null, null);

        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class,
                () -> productService.listProduct(user1, product));
        Assert.assertEquals("Product must have at least 1 category.", thrown.getMessage());
    }

    @Test
    public void test_listProduct_Should_Return_Null_If_RateService_CreateRate_Rate_Id_Is_Null() throws SQLException  {
        User user1 = new User(1, "fullName", "111111111-11", "email1@gmail.com",
                "P@ssword1", LocalDate.now());
        Rate rate1 = new Rate(null, 0, 0);
        Product product = new Product(1, "name", 100.00, "description",
                10, user1, rate1);
        product.addCategory(new Category(1, "category"));

        Mockito.when(rateService.createRate()).thenReturn(rate1);
        Assert.assertNull(productService.listProduct(user1, product));
        Mockito.verify(conn, Mockito.times(1)).rollback();
    }

    @Test
    public void test_listProduct_Should_Return_Null_If_ProductDao_insertProduct_Returns_Null() throws SQLException {
        User user1 = new User(1, "fullName", "111111111-11", "email1@gmail.com",
                "P@ssword1", LocalDate.now());
        Rate rate1 = new Rate(1, 0, 0);
        Product product = new Product(1, "name", 100.00, "description",
                10, user1, rate1);
        product.addCategory(new Category(1, "category"));

        Mockito.when(rateService.createRate()).thenReturn(rate1);
        Mockito.when(productDao.insertProduct(product)).thenReturn(null);
        Assert.assertNull(productService.listProduct(user1, product));
        Mockito.verify(conn, Mockito.times(1)).rollback();
    }

    @Test
    public void test_listProduct_Should_Return_Null_If_auxProductCategory_insert_Returns_False() throws SQLException {
        User user1 = new User(1, "fullName", "111111111-11", "email1@gmail.com",
                "P@ssword1", LocalDate.now());
        Rate rate1 = new Rate(1, 0, 0);
        Product product = new Product(1, "name", 100.00, "description",
                10, user1, rate1);
        product.addCategory(new Category(1, "category"));

        Mockito.when(rateService.createRate()).thenReturn(rate1);
        Mockito.when(productDao.insertProduct(product)).thenReturn(1);
        Mockito.when(auxProductCategoryDao.insert(product.getProductId(),
                product.getCategories().get(0).getCategoryId())).thenReturn(false);
        Assert.assertNull(productService.listProduct(user1, product));
        Mockito.verify(conn, Mockito.times(1)).rollback();
    }

    @Test
    public void test_listProduct_Should_Return_Product_Id_On_Successful_Product_Insert() throws SQLException {
        User user1 = new User(1, "fullName", "111111111-11", "email1@gmail.com",
                "P@ssword1", LocalDate.now());
        Rate rate1 = new Rate(1, 0, 0);
        Product product = new Product(1, "name", 100.00, "description",
                10, user1, rate1);
        product.addCategory(new Category(1, "category"));

        Mockito.when(rateService.createRate()).thenReturn(rate1);
        Mockito.when(productDao.insertProduct(product)).thenReturn(1);
        Mockito.when(auxProductCategoryDao.insert(product.getProductId(),
                product.getCategories().get(0).getCategoryId())).thenReturn(true);
        Assert.assertEquals(Integer.valueOf(1), productService.listProduct(user1, product));
        Mockito.verify(conn, Mockito.times(1)).commit();

    }

    @Test
    public void test_editProduct_Should_Throw_ResourceNotFoundException_If_ProductDao_getProductById_Returns_Null() throws SQLException {
        int nonexistentProductId = 9;
        User user1 = new User(1, "fullName", "111111111-11", "email1@gmail.com",
                "P@ssword1", LocalDate.now());
        Product product = new Product(1, "name", 100.00, "description",
                10, user1, null);

        Mockito.when(productDao.getProductById(nonexistentProductId)).thenReturn(null);
        ResourceNotFoundException thrown = Assert.assertThrows(ResourceNotFoundException.class, () ->
                productService.editProduct(user1, product, nonexistentProductId));
        Assert.assertEquals("Product id " + nonexistentProductId + " not found", thrown.getMessage());
    }

    @Test
    public void test_editProduct_Should_Throw_UnauthorizedException_If_Persisted_Product_User_Is_Different_From_Request_User() {
        User user1 = new User(1, "fullName", "111111111-11", "email1@gmail.com",
                "P@ssword1", LocalDate.now());
        User user2 = new User(2, "fullName", "222222222-22", "email2@gmail.com",
                "P@ssword2", LocalDate.now());
        Rate rate1 = new Rate(1, 1, 0);
        Product product1 = new Product(1, "name", 100.00, "description", 10,
                user1, rate1);
        Product product2 = new Product(null, "product", 50.00, "description", 5,
                null, null);

        Mockito.when(productDao.getProductById(1)).thenReturn(product1);

        UnauthorizedException thrown = Assert.assertThrows(UnauthorizedException.class,
                () -> productService.editProduct(user2, product1, 1));
        Assert.assertEquals("Can't edit another user's product", thrown.getMessage());
    }

    @Test
    public void test_editProduct_Should_Return_True_On_Successful_Update() throws SQLException {
        User user1 = new User(1, "fullName", "111111111-11", "email1@gmail.com",
                "P@ssword1", LocalDate.now());
        Rate rate1 = new Rate(1, 1, 0);
        Product product1 = new Product(1, "name", 100.00, "description", 10,
                user1, rate1);
        Product product2 = new Product(null, "product", 50.00, "description", 5,
                null, null);

        Mockito.when(productDao.getProductById(1)).thenReturn(product1);
        Mockito.when(productDao.updateProduct(product2)).thenReturn(true);

        Assert.assertTrue(productService.editProduct(user1, product2, 1));
        Mockito.verify(conn, Mockito.times(1)).commit();

    }

}