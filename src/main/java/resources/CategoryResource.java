package resources;

import dao.connection.DatabaseConnection;
import dao.connection.database.MySQLPoolConnection;
import entities.Category;
import resources.util.ResourceUtil;
import services.impl.CategoryServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class CategoryResource extends HttpServlet {

    private Connection conn;
    private CategoryServiceImpl categoryService;

    public CategoryResource() {

    }

    //localhost:8080/back/categories/

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            conn = new MySQLPoolConnection().getDatabaseConnection();
            categoryService = new CategoryServiceImpl(conn);

            List<Category> categories = categoryService.getAllCategories();
            ResourceUtil.sendJson(categories, 200, response);

        } finally {
            DatabaseConnection.closeDatabaseConnection(conn);
        }
    }
}
