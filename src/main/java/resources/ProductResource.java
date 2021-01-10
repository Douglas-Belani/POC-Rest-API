package resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import dao.connection.DatabaseConnection;
import dao.connection.database.MySQLPoolConnection;
import entities.Product;
import entities.User;
import resources.exception.ResourceNotFoundException;
import resources.exception.handler.ResourceExceptionHandler;
import resources.util.ResourceUtil;
import services.IProductService;
import services.IUserService;
import services.exceptions.UnauthorizedException;
import services.impl.EmailServiceImpl;
import services.impl.ProductService;
import services.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductResource extends HttpServlet {

    private Connection conn;
    private IProductService productService;
    private IUserService userService;

    public ProductResource() {

    }

    // localhost:8080/back/products/{id}
    // localhost:8080/back/products?page=int&price=double&category=int&category=int
    // localhost:8080/back/products?page=int
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        conn = new MySQLPoolConnection().getDatabaseConnection();
        productService = new ProductService(conn);
        userService = new UserServiceImpl(conn, new EmailServiceImpl());
        try {
            String[] paths = request.getRequestURI().split("/");
            String json;

            if (paths.length == 4) {
                Integer id = Integer.parseInt(paths[3]);
                Product product = productService.getProductById(id);
                json = ResourceUtil.getJsonFromObject(product);

            } else {
                String stringPage = request.getParameter("page");
                String stringPrice = request.getParameter("price");
                String name = request.getParameter("name");
                String[] stringCategories = request.getParameterValues("category");

                if (stringPage == null) {
                    List<Product> products = productService.getAllProducts();
                    json = ResourceUtil.getJsonFromObject(products);
                    response.getOutputStream().print(json);
                    response.setStatus(200);
                    return;
                }

                if (name != null) {
                    int page = Integer.parseInt(stringPage);
                    List<Product> products =
                            productService.searchProductsByNameAndPage(name, page);
                    json = ResourceUtil.getJsonFromObject(products);
                    response.getOutputStream().print(json);
                    response.setStatus(200);
                    return;
                }

                if (stringPrice != null && stringCategories != null) {
                    int page = Integer.parseInt(stringPage);
                    double price = Double.parseDouble(stringPrice);
                    List<Integer> categoriesIds = new ArrayList<>();

                    for (String stringCategoryId : stringCategories) {
                        int categoryId = Integer.parseInt(stringCategoryId);
                        categoriesIds.add(categoryId);
                    }

                    List<Product> products = productService
                            .searchProductsByPriceAndCategoriesAndPage(categoriesIds,
                            price, page);

                    json = ResourceUtil.getJsonFromObject(products);

                } else if (stringPrice != null) {
                    int page = Integer.parseInt(stringPage);
                    double price = Double.parseDouble(stringPrice);

                    List<Product> products = productService
                            .searchProductsByPriceAndPage(price, page);
                    json = ResourceUtil.getJsonFromObject(products);

                } else if (stringCategories != null) {
                    int page = Integer.parseInt(stringPage);
                    List<Integer> categoriesIds = new ArrayList<>();
                    for (String stringCategoryId : stringCategories) {
                        Integer categoryId = Integer.valueOf(stringCategoryId);
                        categoriesIds.add(categoryId);
                    }

                    List<Product> products = productService
                            .searchProductsByCategoriesAndPage(categoriesIds, page);
                    json = ResourceUtil.getJsonFromObject(products);

                } else {
                    int page = Integer.parseInt(stringPage);
                    List<Product> products = productService.searchProductsByPage(page);
                    json = ResourceUtil.getJsonFromObject(products);
                }
            }

            response.getOutputStream().print(json);
            response.setStatus(200);

        } catch (NumberFormatException e) {
            ResourceExceptionHandler exceptionHandler =
                    new ResourceExceptionHandler(e, 400, "Id must be a number.", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 400, response);

        } catch (ResourceNotFoundException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(404,
                    "Resource not found", e.getMessage(), request.getRequestURI());
        ResourceUtil.sendJson(exceptionHandler, 404, response);

        } finally {
            DatabaseConnection.closeDatabaseConnection(conn);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        conn = new MySQLPoolConnection().getDatabaseConnection();
        productService = new ProductService(conn);
        userService = new UserServiceImpl(conn, new EmailServiceImpl());
        try {
            String json = ResourceUtil.getJsonFromRequestBody(request);
            Product product = (Product) ResourceUtil.getObjectFromJson(Product.class, json);
            User user = ResourceUtil.getUserFromAuthorizationHeader(request, userService);

            Integer productId = productService.listProduct(user, product);

            if (productId != null) {
                response.setHeader("Location",
                        request.getContextPath() + "/products/" + productId);
                response.setStatus(201);

            } else {
                response.setStatus(500);
            }

        } catch (IllegalArgumentException | JsonProcessingException e) {
            ResourceExceptionHandler exceptionHandler;
            if (e.getCause() == null) {
                exceptionHandler = new ResourceExceptionHandler(400, e.getMessage(),
                        "Bad arguments", request.getRequestURI());

            } else {
                exceptionHandler = new ResourceExceptionHandler(400, "Bad arguments.",
                        e.getCause().getMessage(), request.getRequestURI());
            }
            ResourceUtil.sendJson(exceptionHandler, 400, response);

        } catch (SQLException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(500,
                    "Internal Server Error", "", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 500, response);

        } finally {
            DatabaseConnection.closeDatabaseConnection(conn);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        conn = new MySQLPoolConnection().getDatabaseConnection();
        productService = new ProductService(conn);
        userService = new UserServiceImpl(conn, new EmailServiceImpl());
        try {
            User user = ResourceUtil.getUserFromAuthorizationHeader(request, userService);

            String json = ResourceUtil.getJsonFromRequestBody(request);
            Product updatedProduct = (Product) ResourceUtil.getObjectFromJson(Product.class, json);

            String[] paths = request.getRequestURI().split("/");
            Integer id = Integer.valueOf(paths[3]);

            boolean updated = productService.editProduct(user, updatedProduct, id);

            if (updated) {
                response.setStatus(204);

            } else {
                response.setStatus(500);
            }

        } catch (UnauthorizedException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(e, 403,
                    "Can't access another user's information.", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 403, response);

        } catch (JsonProcessingException | IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            ResourceExceptionHandler exceptionHandler;
            if (e.getCause() == null) {
                exceptionHandler = new ResourceExceptionHandler(400, e.getMessage(),
                        "Bad arguments", request.getRequestURI());

            } else {
                exceptionHandler = new ResourceExceptionHandler(400, "Bad arguments.", e.getCause().getMessage(),
                        request.getRequestURI());
            }
            ResourceUtil.sendJson(exceptionHandler, 400, response);

        } catch (SQLException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(500,
                    "Internal Server Error", "", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 500, response);

        } catch (ResourceNotFoundException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(404,
                    "Resource not found", e.getMessage(), request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 404, response);
        } finally {
            DatabaseConnection.closeDatabaseConnection(conn);
        }
    }
}