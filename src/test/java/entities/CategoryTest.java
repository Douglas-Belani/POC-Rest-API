package entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CategoryTest {

    private Category category;

    @Before
    public void initialize() {
        category = new Category(1, "category1");
    }

    @Test
    public void test_setName_Should_Throw_IllegalArgumentException_When_Null_Name_Is_Passed() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                category.setName(null));
        Assert.assertEquals("Name must not be blank.", thrown.getMessage());
    }

    @Test
    public void test_setName_Should_Throw_IllegalArgumentException_When_Blank_Name_Is_Passed() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                category.setName(""));
        Assert.assertEquals("Name must not be blank.", thrown.getMessage());
    }

    @Test
    public void test_setName_Should_Throw_IllegalArgumentException_When_Name_Is_Larger_Than_120_Characters() {
        StringBuilder sb = new StringBuilder();
        while (sb.length() <= 120) {
            sb.append("a");
        }

        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                category.setName(sb.toString()));
        Assert.assertEquals("Name must have less than 120 characters.", thrown.getMessage());
    }

    @Test
    public void test_setName_Should_Change_Name_Successfully_When_Non_Null_And_Non_Blank_Name_With_Less_Than_120_Characters_Is_Passsed() {
        String newName = "newName";
        category.setName(newName);
        Assert.assertEquals(newName, category.getName());
    }

}
