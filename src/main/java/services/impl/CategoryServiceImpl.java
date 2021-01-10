package services.impl;

import dao.ICategoryDao;
import dao.impl.CategoryDaoImpl;
import entities.Category;
import services.ICategoryService;

import java.sql.Connection;
import java.util.List;

public class CategoryServiceImpl implements ICategoryService {

    private Connection conn;
    private ICategoryDao categoryDao;

    public CategoryServiceImpl(Connection conn) {
        this.conn = conn;
        this.categoryDao = new CategoryDaoImpl(this.conn);
    }


    @Override
    public List<Category> getAllCategories() {
        return categoryDao.getAllCategories();
    }
}
