package security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Properties;

public class JWTUtil {

    public static String generateJWT(String email) {
        return JWT.create().withExpiresAt(Date.valueOf(LocalDate.now().plusDays(1)))
                .withClaim("email", email).sign(getAlgorithm());
    }

    public static DecodedJWT verifyJWT(String token) {
        try {
            JWTVerifier verifier = JWT.require(getAlgorithm()).build();
            return verifier.verify(token);

        } catch (JWTVerificationException e) {
        throw  e;
        }

    }

    public static DecodedJWT getDecodedJWT(String token) {
        return JWT.decode(token);
    }

    private static Properties getSecret() {
        try (InputStream is = JWTUtil.class.getResourceAsStream("/jwt_secret.properties")) {
            Properties properties = new Properties();
            properties.load(is);

            return properties;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Algorithm getAlgorithm() {
        Properties properties = getSecret();

        if (properties != null) {
          return Algorithm.HMAC256(Base64.getEncoder()
                    .encodeToString(getSecret().getProperty("jwtSecret").getBytes()));

        } else {
            return null;
        }
    }

}