package resources.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import dao.connection.database.MySQLPoolConnection;
import dao.impl.UserDaoImpl;
import resources.exception.UnauthenticatedException;
import resources.exception.handler.ResourceExceptionHandler;
import resources.util.ResourceUtil;
import security.Authorization;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthorizationFilter implements Filter {

    private Authorization authorization;

    public AuthorizationFilter() {
        authorization = new Authorization(
                new UserDaoImpl(new MySQLPoolConnection().getDatabaseConnection()));
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;

        Matcher productMatcher = Pattern.compile("^\\/back\\/products\\/?\\w*$")
                .matcher(((HttpServletRequest) request).getRequestURI());
        Matcher userMatcher = Pattern.compile("^\\/back\\/users\\/?$")
                .matcher(((HttpServletRequest) request).getRequestURI());

        if (productMatcher.find() && servletRequest.getMethod().equals("GET")) {
            chain.doFilter(request, response);
            return;
        }

        if (userMatcher.find() && servletRequest.getMethod().equals("POST")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            if (authorization.isAuthenticated(servletRequest)) {
                chain.doFilter(request, response);

            } else {
                throw new UnauthenticatedException("Must login first.");
            }

        } catch (JWTVerificationException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(e, 401, "",
                    ((HttpServletRequest) request).getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 401, (HttpServletResponse) response);
            ((HttpServletResponse)response).setHeader("Location",
                    ((HttpServletRequest)request).getContextPath() + "/login");

        } catch (UnauthenticatedException e) {
            ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler(e, 401,
                    "Must login first.", ((HttpServletRequest) request).getRequestURI());
            ResourceUtil.sendJson(exceptionHandler, 401, (HttpServletResponse) response);
            ((HttpServletResponse)response).setHeader("Location",
                    ((HttpServletRequest)request).getContextPath() + "/login");
        }
    }

    @Override
    public void destroy() {

    }
}
