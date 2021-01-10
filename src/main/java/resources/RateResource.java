package resources;

import dao.connection.DatabaseConnection;
import dao.connection.database.MySQLPoolConnection;
import entities.User;
import resources.exception.ResourceNotFoundException;
import resources.exception.handler.ResourceExceptionHandler;
import resources.util.ResourceUtil;
import services.IRateService;
import services.IUserService;
import services.impl.EmailServiceImpl;
import services.impl.RateServiceImpl;
import services.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class RateResource extends HttpServlet {

    private Connection conn;
    private IRateService rateService;
    private IUserService userService;

    public RateResource() {

    }

    //localhost:8080/back/rate/upvote/{id}
    //loclahost:8080/back/rate/downvote/{id}
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        conn = new MySQLPoolConnection().getDatabaseConnection();
        rateService = new RateServiceImpl(conn);
        userService = new UserServiceImpl(conn, new EmailServiceImpl());

        try {
            String[] paths = request.getRequestURI().split("/");
            User user = ResourceUtil.getUserFromAuthorizationHeader(request, userService);

            int rateId = Integer.parseInt(paths[4]);
            boolean updated;
            if (paths[3].equals("upvote")) {
                updated =  rateService.upvote(user.getUserId(), rateId);

            } else if (paths[3].equals("downvote")) {
                updated = rateService.downvote(user.getUserId(), rateId);

            } else {
                response.setStatus(400);
                return;
            }

            if (updated) {
                response.setStatus(204);

            } else {
                response.setStatus(500);
            }

        } catch (NumberFormatException e) {
            ResourceExceptionHandler exceptionHandler =
                    new ResourceExceptionHandler(e, 400, "Id must be a number.", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 400, response);

        } catch (ArrayIndexOutOfBoundsException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(400, e.getMessage(),
                    "", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 400, response);

        } catch (ResourceNotFoundException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(404,
                    "Resource not found", e.getMessage(), request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 404, response);

        } catch (SQLException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(500,
                    "Internal Server Error", "", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 500, response);

        } finally {
            DatabaseConnection.closeDatabaseConnection(conn);
        }
    }
}
