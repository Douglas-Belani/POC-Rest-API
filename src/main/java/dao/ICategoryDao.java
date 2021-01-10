package dao;

import entities.Category;

import java.util.List;

public interface ICategoryDao {

    public abstract List<Category> getAllCategories();

    public abstract Category getCategoryById(int id);

    public abstract Integer insertCategory(Category category);

    public abstract boolean updateCategory(Category category);

    public abstract boolean deleteCategoryById(int id);

}
