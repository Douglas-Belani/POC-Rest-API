package entities;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductTest {

    private Product product;

    @Before
    public void initialize() {
        product = new Product(1, "name", 100.00,
                "description", 12, null, null);
    }

    @Test
    public void test_setName_Should_Throw_IllegalArgumentException_For_Null_Name() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                product.setName(null));
        Assert.assertEquals("Name must not be blank.", thrown.getMessage());
    }

    @Test
    public void test_setName_Should_Throw_IllegalArgumentException_For_Blank_Name() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                product.setName(""));
        Assert.assertEquals("Name must not be blank.", thrown.getMessage());
    }

    @Test
    public void test_setName_Should_Throw_IllegalArgumentException_For_Name_Larger_Than_120_Characters(){
        StringBuilder sb = new StringBuilder();
        while (sb.length() <= 120) {
            sb.append("a");
        }

        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                product.setName(sb.toString()));
        Assert.assertEquals("Name must have less than 120 characters.", thrown.getMessage());
    }

    @Test
    public void test_setName_Should_Successfully_Change_Name_For_Non_Null_Non_Blank_Name_With_Less_Than_120_Characters() {
        String newName = "newName";
        product.setName(newName);
        Assert.assertEquals(newName, product.getName());
    }

    @Test
    public void test_setPrice_Should_Throw_IllegalArgumentException_For_Price_Less_Than_Or_Equal_To_0_0() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                product.setPrice(-0.1));
        Assert.assertEquals("Price must be greater than 0.0.", thrown.getMessage());
    }

    @Test
    public void test_setPrice_Should_Successfully_Change_Price_For_Positive_Price() {
        double newPrice = 75.0;
        product.setPrice(newPrice);
        Assert.assertEquals(newPrice, product.getPrice(), 0.01);
    }

    @Test
    public void test_setDescription_Should_Throw_IllegalArgumentException_For_Null_Description() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                product.setDescription(null));
        Assert.assertEquals("Description must not be blank.", thrown.getMessage());
    }

    @Test
    public void test_setDescription_Should_Throw_IllegalArgumentException_For_Blank_Description() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                product.setDescription(""));
        Assert.assertEquals("Description must not be blank.", thrown.getMessage());
    }

    @Test
    public void test_setDescription_Should_Throw_IllegalArgumentException_For_Description_Larger_Than_140_Characters() {
        StringBuilder sb = new StringBuilder();
        while (sb.length() <= 140) {
            sb.append("a");
        }
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                product.setDescription(sb.toString()));
        Assert.assertEquals("Description must have less than 140 characters.", thrown.getMessage());
    }

    @Test
    public void test_setDescription_Should_Successfully_Change_Description_For_Non_Null_Non_Blank_Description_With_Less_Than_140_Characters() {
        String newDescription = "newDescription";
        product.setDescription(newDescription);
        Assert.assertEquals(newDescription, product.getDescription());
    }

    @Test
    public void test_setStock_Should_Throw_IllegalArgumentException_For_Negative_Stock() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                product.setStock(-1));
        Assert.assertEquals("Quantity must be greater than 0.", thrown.getMessage());
    }

    @Test
    public void test_setStock_Should_Successfully_Change_Stock_For_Positive_Stock() {
        int stock = 5;
        product.setStock(stock);
        Assert.assertEquals(stock, product.getStock());
    }

    @Test
    public void test_setUser_Should_Throw_IllegalArgumentException_For_Null_User() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                product.setUser(null));
        Assert.assertEquals("User must not be null.", thrown.getMessage());
    }

    @Test
    public void test_setUser_Should_Successfully_Change_User_For_Non_Null_User() {
        User user = new User(1, "fullName", "111111111-11",
                "email@gmail.com", "password", LocalDate.now());
        product.setUser(user);
        Assert.assertEquals(user, product.getUser());
    }

    @Test
    public void test_setRate_Should_Throw_IllegalArgumentException_For_Null_Rate() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                product.setRate(null));
        Assert.assertEquals("Rate must not be null.", thrown.getMessage());
    }

    @Test
    public void test_setRate_Should_Successfully_Change_Rate_For_Non_Null_Rate() {
        Rate rate = new Rate(1, 1, 0);
        product.setRate(rate);
        assertThat(rate, samePropertyValuesAs(product.getRate()));
    }

    @Test
    public void test_setCategories_Should_Throw_IllegalArgumentException() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                product.setCategories(null));
        Assert.assertEquals("Categories must not be null.", thrown.getMessage());
    }

    @Test
    public void test_setCategories_Should_Successfully_Change_Categories_For_Non_Null_Categories_List() {
        Category category1 = new Category(1, "category1");
        Category category2 = new Category(2, "category2");
        List<Category> categories = new ArrayList<>();
        categories.addAll(Arrays.asList(category1, category2));
        product.setCategories(categories);
        assertThat(categories, samePropertyValuesAs(product.getCategories()));
    }

    @Test
    public void test_addCategory_Should_Throw_IllegalArgumentException_For_Null_Category() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                product.addCategory(null));
        Assert.assertEquals("Category must not be null.", thrown.getMessage());
    }

    @Test
    public void test_addCategory_Should_Successfully_Add_Category_To_Categories_For_Non_Null_Category() {
        product.getCategories().clear();
        Assert.assertEquals(0, product.getCategories().size());
        Category category = new Category(1, "category1");
        product.addCategory(category);
        Assert.assertEquals(1, product.getCategories().size());
        Assert.assertEquals(category, product.getCategories().get(0));
    }

    @Test
    public void test_addComment_Should_Throw_IllegalArgumentException_For_Null_Comment() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                product.addComment(null));
        Assert.assertEquals("Comment must not be empty.", thrown.getMessage());
    }

    @Test
    public void test_addComment_Should_Successfully_Add_Comment_To_Comments_For_Non_Null_Comment() {
        Comments comments = new TopLevelComment(1, "text", null,
                null);
        Assert.assertEquals(0, product.getComments().size());
        product.addComment(comments);
        Assert.assertEquals(1, product.getComments().size());
        assertThat(comments, samePropertyValuesAs(product.getComments().get(0)));
    }

}
