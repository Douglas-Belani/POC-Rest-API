package resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import dao.connection.DatabaseConnection;
import dao.connection.database.MySQLPoolConnection;
import entities.Address;
import entities.User;
import resources.exception.ResourceNotFoundException;
import resources.exception.handler.ResourceExceptionHandler;
import resources.util.ResourceUtil;
import services.IAddressService;
import services.IUserService;
import services.exceptions.UnauthorizedException;
import services.impl.AddressServiceImpl;
import services.impl.EmailServiceImpl;
import services.impl.UserServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AddressResource extends HttpServlet {

    private Connection conn;
    private IAddressService addressService;
    private IUserService userService;

    public AddressResource() {

    }

    //localhost:8080/users/addresses/{id}
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            this.conn = new MySQLPoolConnection().getDatabaseConnection();
            this.addressService = new AddressServiceImpl(conn);
            this.userService = new UserServiceImpl(conn, new EmailServiceImpl());

            User user = ResourceUtil.getUserFromAuthorizationHeader(request, userService);
            String json;

            String[] paths = request.getRequestURI().split("/");
            if (paths.length == 5) {
                Integer id = Integer.valueOf(paths[4]);

                Address address = addressService.getAddressById(id, user);
                json = ResourceUtil.getJsonFromObject(address);

            } else {
                List<Address> addresses = addressService.getAllAddressByUser(user);
                json = ResourceUtil.getJsonFromObject(addresses);
            }
            response.getOutputStream().print(json);
            response.setStatus(200);

        } catch (NumberFormatException e) {
            ResourceExceptionHandler exceptionHandler =
                    new ResourceExceptionHandler(e, 400, "Id must be a number.", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 400, response);

        } catch (UnauthorizedException e) {
            ResourceExceptionHandler exceptionHandler =
                    new ResourceExceptionHandler(e, 403, "Can't access another user's information.",
                            request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 403, response);

        } catch (ResourceNotFoundException e) {
            ResourceExceptionHandler exceptionHandler =
                    new ResourceExceptionHandler(404, "Resource not found", e.getMessage(),
                            request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 404, response);
        }

        finally {
            DatabaseConnection.closeDatabaseConnection(conn);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            this.conn = new MySQLPoolConnection().getDatabaseConnection();
            this.addressService = new AddressServiceImpl(conn);
            this.userService = new UserServiceImpl(conn, new EmailServiceImpl());

            String json = ResourceUtil.getJsonFromRequestBody(request);
            Address address = (Address) ResourceUtil.getObjectFromJson(Address.class, json);
            User user = ResourceUtil.getUserFromAuthorizationHeader(request, userService);

            Integer addressId = addressService.insertAddress(address, user);

            if (addressId != null) {
                response.setHeader("Location",
                        "back/users/addresses/" + addressId);
                response.setStatus(201);
            } else {
                response.setStatus(500);
            }

        } catch (JsonProcessingException | IllegalArgumentException e) {
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

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            this.conn = new MySQLPoolConnection().getDatabaseConnection();
            this.addressService = new AddressServiceImpl(conn);
            this.userService = new UserServiceImpl(conn, new EmailServiceImpl());

            Integer userId = Integer.valueOf(request.getRequestURI().split("/")[4]);
            String json = ResourceUtil.getJsonFromRequestBody(request);
            Address updatedAddress = (Address) ResourceUtil.getObjectFromJson(Address.class, json);
            User user = ResourceUtil.getUserFromAuthorizationHeader(request, userService);

            boolean updated = addressService.updateAddress(userId, updatedAddress, user);

            if (updated) {
                response.setStatus(204);

            } else {
                response.setStatus(500);
            }

        } catch (UnauthorizedException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(e, 403,
                    "Can't access another user's information.", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 403, response);

        } catch (IllegalArgumentException |
                JsonProcessingException e) {
            ResourceExceptionHandler exceptionHandler;
            if (e.getCause() == null) {
                exceptionHandler = new ResourceExceptionHandler(400, e.getMessage(),
                        "Bad arguments", request.getRequestURI());

            } else {
                exceptionHandler = new ResourceExceptionHandler(400, "Bad arguments.", e.getCause().getMessage(),
                        request.getRequestURI());
            }
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

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            this.conn = new MySQLPoolConnection().getDatabaseConnection();
            this.addressService = new AddressServiceImpl(conn);
            this.userService = new UserServiceImpl(conn, new EmailServiceImpl());

            Integer id = Integer.valueOf(request.getRequestURI().split("/")[4]);
            User user = ResourceUtil.getUserFromAuthorizationHeader(request, userService);

            boolean deleted = addressService.deleteAddress(id, user);

            if (deleted) {
                response.setStatus(204);

            } else {
                response.setStatus(500);
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(400, e.getMessage(),
                    "", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 400, response);

        } catch (NumberFormatException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(e, 400,
                    "Id must be a number", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 400, response);

        }  catch (ResourceNotFoundException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(404,
                    "Resource not found", e.getMessage(), request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 404, response);
    } catch (UnauthorizedException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(e, 403,
                    "Can't access another user's information.", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 403, response);

        } finally {
            DatabaseConnection.closeDatabaseConnection(conn);
        }
    }
}