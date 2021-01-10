package resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.connection.DatabaseConnection;
import dao.connection.database.MySQLPoolConnection;
import entities.User;
import resources.exception.ResourceNotFoundException;
import resources.exception.handler.ResourceExceptionHandler;
import resources.util.ResourceUtil;
import services.ICommentService;
import services.IUserService;
import services.exceptions.UnauthorizedException;
import services.impl.CommentService;
import services.impl.EmailServiceImpl;
import services.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class CommentResource extends HttpServlet {

    private Connection conn;
    private ICommentService commentService;
    private IUserService userService;

    public CommentResource() {

    }

    //localhost:8080/back/comments/{id}
    //localhost:8080/back/comments/products/{productId}
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        conn = new MySQLPoolConnection().getDatabaseConnection();
        commentService = new CommentService(conn);
        userService = new UserServiceImpl(conn, new EmailServiceImpl());
        try {
            String[] paths = request.getRequestURI().split("/");
            Integer productId = Integer.valueOf(paths[4]);

            User user = ResourceUtil.getUserFromAuthorizationHeader(request, userService);

            String json = ResourceUtil.getJsonFromRequestBody(request);
            ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);
            String text = node.has("text") ? node.get("text").asText() : null;
            Integer topLevelCommentId = node.has("topLevelCommentId") ?
                    (node.get("topLevelCommentId").asInt()) : null;

            if (text == null) {
                throw new IllegalArgumentException("Text must not be blank.");
            }

            if (topLevelCommentId != null && topLevelCommentId.equals(0)) {
                topLevelCommentId = null;
            }

            Integer commentId = commentService.comment(text, user, productId, topLevelCommentId);
            if (commentId != null) {
                response.setStatus(201);
                response.setHeader("Location", "/back/products/" + productId);

            } else {
                response.setStatus(500);
            }

        } catch (NumberFormatException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(e, 400,
                    "Id must be a number", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 400, response);

        } catch (ArrayIndexOutOfBoundsException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(400, e.getMessage(),
                    "Bad Request URL", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 400, response);

        } catch (IllegalArgumentException | JsonProcessingException e) {
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
        conn = new MySQLPoolConnection().getDatabaseConnection();
        commentService = new CommentService(conn);
        userService = new UserServiceImpl(conn, new EmailServiceImpl());
        try {
            String[] paths = request.getRequestURI().split("/");
            Integer commentId = Integer.valueOf(paths[3]);

            User user = ResourceUtil.getUserFromAuthorizationHeader(request, userService);

            String json = ResourceUtil.getJsonFromRequestBody(request);
            ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);
            String text = node.has("text") ? node.get("text").asText() : null;

            if (text == null) {
                throw new IllegalArgumentException("Text must not be blank.");
            }

            boolean updated = commentService.editComment(user, text, commentId);

            if (updated) {
                response.setStatus(204);

            } else {
                response.setStatus(500);
            }

        } catch (NumberFormatException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(e, 400,
                    "Id must be a number", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 400, response);

        }  catch (IllegalArgumentException | JsonProcessingException e) {
            ResourceExceptionHandler exceptionHandler;
            if (e.getCause() == null) {
                exceptionHandler = new ResourceExceptionHandler(400, e.getMessage(),
                        "Bad arguments", request.getRequestURI());

            } else {
                exceptionHandler = new ResourceExceptionHandler(400, "Bad arguments.", e.getCause().getMessage(),
                        request.getRequestURI());
            }
            ResourceUtil.sendJson(exceptionHandler, 400, response);

        } catch (UnauthorizedException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(e, 403,
                    "Can't access another user's information.", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 403, response);

        } catch (ArrayIndexOutOfBoundsException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(400, e.getMessage(),
                    "Bad Request URL", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 400, response);

        } catch (ResourceNotFoundException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(404,
                    "Resource not found", e.getMessage(),
                        request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 404, response);
        } finally {
            DatabaseConnection.closeDatabaseConnection(conn);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        conn = new MySQLPoolConnection().getDatabaseConnection();
        commentService = new CommentService(conn);
        userService = new UserServiceImpl(conn, new EmailServiceImpl());
        try {
            String[] paths = request.getRequestURI().split("/");
            Integer commentid = Integer.valueOf(paths[3]);

            User user = ResourceUtil.getUserFromAuthorizationHeader(request, userService);

            boolean deleted = commentService.deleteComment(user, commentid);

            if (deleted) {
                response.setStatus(204);

            } else {
                response.setStatus(500);
            }

        } catch (SQLException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(500,
                    "Internal Server Error", "", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 500, response);

        } catch (NumberFormatException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(e, 400,
                    "Id must be a number", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 400, response);

        } catch (UnauthorizedException e) {
        ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(e, 403,
                "Can't access another user's information.", request.getRequestURI());
        ResourceUtil.sendJson(exceptionHandler, 403, response);

        } catch (ArrayIndexOutOfBoundsException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(400, e.getMessage(),
                    "Bad Request URL", request.getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 400, response);

        } catch (ResourceNotFoundException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(404,
                    "Resource not found", e.getMessage(), request.getRequestURI());
             ResourceUtil.sendJson(exceptionHandler, 404, response);

        } finally {
            DatabaseConnection.closeDatabaseConnection(conn);
        }
    }
}