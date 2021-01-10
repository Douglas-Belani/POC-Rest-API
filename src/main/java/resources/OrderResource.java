package resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import dao.connection.DatabaseConnection;
import dao.connection.database.MySQLPoolConnection;
import entities.Order;
import entities.User;
import resources.exception.ResourceNotFoundException;
import resources.exception.handler.ResourceExceptionHandler;
import resources.util.ResourceUtil;
import services.IOrderService;
import services.IUserService;
import services.exceptions.UnauthorizedException;
import services.impl.EmailServiceImpl;
import services.impl.OrderServiceImpl;
import services.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderResource extends HttpServlet {

    private Connection conn;
    private IOrderService orderService;
    private IUserService userService;

    public OrderResource() {

    }

    // localhost:8080/back/users/orders/{id}
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.conn = new MySQLPoolConnection().getDatabaseConnection();
        this.orderService = new OrderServiceImpl(conn);
        this.userService = new UserServiceImpl(conn, new EmailServiceImpl());

        try {
            User user = ResourceUtil.getUserFromAuthorizationHeader(request, userService);
            String json;

            String[] paths = request.getRequestURI().split("/");
            if (paths.length == 5) {
                Integer id = Integer.valueOf(paths[4]);
                Order order = orderService.getOrderById(user, id);
                json = ResourceUtil.getJsonFromObject(order);

            } else {
                List<Order> orders = orderService.getUserOrders(user);
                json = ResourceUtil.getJsonFromObject(orders);
            }
            response.getOutputStream().print(json);
            response.setStatus(200);

        } catch (NumberFormatException e) {
            ResourceExceptionHandler exceptionHandler =
                    new ResourceExceptionHandler(e, 400, "Id must be a number", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 400, response);

        } catch (UnauthorizedException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(e, 403,
                    "Can't access another user information.", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 403, response);

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
        this.conn = new MySQLPoolConnection().getDatabaseConnection();
        this.orderService = new OrderServiceImpl(conn);
        this.userService = new UserServiceImpl(conn, new EmailServiceImpl());

        try {
            String json = ResourceUtil.getJsonFromRequestBody(request);
            Order order = (Order) ResourceUtil.getObjectFromJson(Order.class, json);

            if (order.getUser() == null) {
                User user = ResourceUtil.getUserFromAuthorizationHeader(request, userService);
                order.setUser(user);
            }

            Integer orderId = orderService.insertOrder(order);
            if (orderId != null) {
                response.setHeader("Location",
                        "back/users/orders/" + orderId);
                response.setStatus(201);

            } else {
                response.setStatus(500);
            }


        } catch (IllegalArgumentException  | JsonProcessingException e) {
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

        } finally {
            DatabaseConnection.closeDatabaseConnection(conn);
        }
    }
}