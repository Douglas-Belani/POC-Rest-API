package resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import dao.connection.DatabaseConnection;
import dao.connection.database.MySQLPoolConnection;
import entities.User;
import resources.exception.handler.ResourceExceptionHandler;
import resources.util.ResourceUtil;
import security.util.JWTUtil;
import services.IProductService;
import services.IUserService;
import services.exceptions.UnauthorizedException;
import services.impl.EmailServiceImpl;
import services.impl.ProductService;
import services.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

public class UserResource extends HttpServlet {

    private Connection conn;
    private IUserService userService;
    private IProductService productService;

    public UserResource() {

    }
    // localhost:8080/users/{id}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            conn = new MySQLPoolConnection().getDatabaseConnection();
            userService = new UserServiceImpl(conn, new EmailServiceImpl());
            productService = new ProductService(conn);

            Integer userId = Integer.valueOf(request.getRequestURI().split("/")[3]);
            User user = ResourceUtil.getUserFromAuthorizationHeader(request, userService);

            if (!user.getUserId().equals(userId)) {
                throw new UnauthorizedException("Can't access another user information");
            }

            user.setListedProducts(productService.getAllProductsByUser(user));
            user.setUpvotedProducts(productService.getUserUpvotedProducts(user));
            ResourceUtil.sendJson(user, 200, response);

        } catch (NumberFormatException e) {
            ResourceExceptionHandler exceptionHandler =
                    new ResourceExceptionHandler(e, 400, "Id must be a number.",
                            request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 400, response);

        } catch (ArrayIndexOutOfBoundsException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(400, e.getMessage(),
                    "Bad Request URL", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 400, response);

        } catch (UnauthorizedException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(e, 403,
                    "Can't access another user information.", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 403, response);

        } finally {
            DatabaseConnection.closeDatabaseConnection(conn);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            conn = new MySQLPoolConnection().getDatabaseConnection();
            userService = new UserServiceImpl(conn, new EmailServiceImpl());
            productService = new ProductService(conn);

            String json = ResourceUtil.getJsonFromRequestBody(request);
            User newUser = (User) ResourceUtil.getObjectFromJson(User.class, json);
            Integer userId = userService.createUser(newUser);

            if (userId != null) {
                String token = "Bearer " + JWTUtil.generateJWT(newUser.getEmail());
                response.setHeader("Authorization", token);
                response.setStatus(201);
                response.setHeader("Location", "back/user/" + userId);

            } else {
                response.setStatus(500);
            }

        } catch (JsonProcessingException | IllegalArgumentException e) {
            ResourceExceptionHandler exceptionHandler;
            if (e.getCause() == null) {
                exceptionHandler = new ResourceExceptionHandler(400, e.getMessage(),
                        "Bad arguments", request.getRequestURI());

            } else {
                exceptionHandler = new ResourceExceptionHandler(400, "Bad arguments.",
                        e.getCause().getMessage(), request.getRequestURI());
            }
            ResourceUtil.sendJson(exceptionHandler, 400, response);

        }finally {
            DatabaseConnection.closeDatabaseConnection(conn);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            conn = new MySQLPoolConnection().getDatabaseConnection();
            userService = new UserServiceImpl(conn, new EmailServiceImpl());
            productService = new ProductService(conn);

            Integer userId = Integer.valueOf(request.getRequestURI().split("/")[3]);
            User oldUser = ResourceUtil.getUserFromAuthorizationHeader(request, userService);
            if (!oldUser.getUserId().equals(userId)) {
                throw new UnauthorizedException("Can't update another user.");
            }

            String json = ResourceUtil.getJsonFromRequestBody(request);
            User updatedUser = (User) ResourceUtil.getObjectFromJson(User.class, json);

            boolean updated = userService.updateUser(oldUser, userId, updatedUser);

            if (updated) {
                String token = "Bearer " +  JWTUtil.generateJWT(updatedUser.getEmail());
                response.setHeader("Authorization", token);
                response.setStatus(204);

            } else {
                response.setStatus(500);
            }

        } catch (JsonProcessingException | IllegalArgumentException e) {
            ResourceExceptionHandler exceptionHandler;
            if (e.getCause() == null) {
                exceptionHandler = new ResourceExceptionHandler(400, e.getMessage(),
                        "Id must be a valid number", request.getRequestURI());

            } else {
                exceptionHandler = new ResourceExceptionHandler(400, "Bad arguments.", e.getCause().getMessage(),
                        request.getRequestURI());
            }
            ResourceUtil.sendJson(exceptionHandler, 400, response);


        } catch (ArrayIndexOutOfBoundsException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(400, e.getMessage(),
                    "Bad Request URL", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 400, response);

        } catch (UnauthorizedException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(e, 403,
                    "Can't access another user information.", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 403, response);

        } finally {
            DatabaseConnection.closeDatabaseConnection(conn);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            conn = new MySQLPoolConnection().getDatabaseConnection();
            userService = new UserServiceImpl(conn, new EmailServiceImpl());
            productService = new ProductService(conn);

            Integer id = Integer.valueOf(request.getRequestURI().split("/")[3]);
            User user = ResourceUtil.getUserFromAuthorizationHeader(request, userService);

            if (!user.getUserId().equals(id)) {
                throw new UnauthorizedException("Can't delete another user.");
            }

            boolean deleted = userService.deleteUserById(user, id);

            if (deleted) {
                response.setStatus(204);

            } else {
                response.setStatus(500);
            }
        } catch (UnauthorizedException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(e, 403,
                    "Can't access another user's information.", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 403, response);

        } catch (ArrayIndexOutOfBoundsException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(400, e.getMessage(),
                    "Bad Request URL", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 400, response);

        } catch (NumberFormatException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(e, 400,
                    "Id must be a number", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 400, response);

        } finally {
            DatabaseConnection.closeDatabaseConnection(conn);
        }
    }
}