package dao.impl;

import connection.H2Connection;
import dao.ICategoryDao;
import entities.Category;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

public class CategoryDaoImplTest {

    private static ICategoryDao categoryDao;

    @BeforeClass
    public static void initialize() {
        categoryDao = new CategoryDaoImpl(H2Connection.getH2DatabaseConnection());
    }

    @Test
    public void test_GetAllCategories_Should_Return_Categories_Id_1_2_3_4() {
        Category expectedCategory1 = new Category(1, "category1");
        Category expectedCategory2 = new Category(2, "category2");
        Category expectedCategory3 = new Category(3, "category3");
        Category expectedCategory4 = new Category(4, "category4");

        List<Category> categories = categoryDao.getAllCategories();

        assertThat(expectedCategory1, samePropertyValuesAs(categories.get(0)));
        assertThat(expectedCategory2, samePropertyValuesAs(categories.get(1)));
        assertThat(expectedCategory3, samePropertyValuesAs(categories.get(2)));
        assertThat(expectedCategory4, samePropertyValuesAs(categories.get(3)));

    }

    @Test
    public void test_GetCategoryById_Should_Return_Category_Id_3() {
        Category categoryExpected = new Category(3, "category3");

        Category category = categoryDao.getCategoryById(3);

        assertThat(categoryExpected, samePropertyValuesAs(category));
    }

    @Test
    public void test_GetCategoryById_Should_Return_Null_For_Nonexistent_Category_Id() {
        int nonExistentCategoryId = 10;

        Category category = categoryDao.getCategoryById(nonExistentCategoryId);
        Assert.assertNull(category);
    }

    @Test
    public void test_InsertCategory_Should_Return_Category_Id() {
        Category newCategory = new Category(null, "category");

        Integer categoryId  = categoryDao.insertCategory(newCategory);
        newCategory.setCategoryId(categoryId);

        Category category = categoryDao.getCategoryById(categoryId);

        assertThat(newCategory, samePropertyValuesAs(category));
    }

    @Test
    public void test_DeleteCategoryById_Should_Return_True_For_Category_Existent_Id() {
        int categoryId = 6;
        boolean deleted = categoryDao.deleteCategoryById(categoryId);
        Assert.assertTrue(deleted);
    }

    @Test
    public void test_DeleteCategoryById_Should_Return_False_For_Nonexistent_Id() {
        int nonExistentCategoryId = 10;
        boolean deleted = categoryDao.deleteCategoryById(nonExistentCategoryId);
        Assert.assertFalse(deleted);
    }

}
