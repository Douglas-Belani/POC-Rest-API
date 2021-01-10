package security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dao.IUserDao;
import resources.exception.UnauthenticatedException;
import resources.exception.UnauthorizedException;
import security.util.JWTUtil;

import javax.servlet.http.HttpServletRequest;

public class Authorization {

    private IUserDao userDao;

    public Authorization(IUserDao userDao) {
        this.userDao = userDao;
    }

    public boolean isAuthenticated(HttpServletRequest request) {
        try {
            String authorization = request.getHeader("Authorization");

            if (authorization == null || !authorization.startsWith("Bearer ")) {
                throw new UnauthenticatedException("User is not authenticated.");
            }

            String token = authorization.substring(7);
            DecodedJWT decodedJWT = JWTUtil.verifyJWT(token);
            return userDao.getUserByEmail(decodedJWT.getClaim("email").asString()) != null;

        } catch (JWTVerificationException | UnauthorizedException e) {
            throw e;
        }
    }
}
