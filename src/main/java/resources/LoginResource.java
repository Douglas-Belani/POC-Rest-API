package resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.IUserDao;
import dao.connection.DatabaseConnection;
import dao.connection.database.MySQLPoolConnection;
import dao.impl.UserDaoImpl;
import resources.exception.handler.ResourceExceptionHandler;
import resources.util.ResourceUtil;
import security.Authentication;
import security.util.JWTUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;;
import java.io.IOException;
import java.sql.Connection;

public class LoginResource extends HttpServlet {

    private Connection conn;
    private Authentication authentication;
    private IUserDao userDao;

    public LoginResource() {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            this.conn = new MySQLPoolConnection().getDatabaseConnection();
            this.userDao = new UserDaoImpl(conn);
            this.authentication = new Authentication(userDao);

            String json = ResourceUtil.getJsonFromRequestBody(request);
            ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);

            String email = node.has("email") ? node.get("email").asText() : null;
            String password = node.has("password") ? node.get("password").asText() : null;

            boolean isAuthenticated = authentication.authenticateUser(email, password);

            if (isAuthenticated) {
                String token = "Bearer " + JWTUtil.generateJWT(email);
                response.setHeader("Authorization", token);
                response.setHeader("Location", "/back/user/" + userDao.getUserByEmail(email).getUserId());
                response.setStatus(200);

            } else {
                ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(401,
                        "User provided wrong credentials", "Incorrect email or password",
                        request.getRequestURI());
                ResourceUtil.sendJson(exceptionHandler, 401, response);
            }

        } finally {
            DatabaseConnection.closeDatabaseConnection(conn);
        }
    }
}
