package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;

public class PasswordUtil {

    private static final byte[] SALT = new byte[32];
    private static final int ITERATIONS = 20000;

    public static String passwordHash(String password) {
        UserUtil.checkPassword(password);
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(SALT);

        try {
            Base64.Encoder enc = Base64.getEncoder();
            String saltPlusSecret = enc.encodeToString(SALT) +
                    enc.encodeToString(getSecret().getProperty("passwordSecret").getBytes());

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hashedSaltPlusSecret = md.digest(saltPlusSecret.getBytes());
            md.update(hashedSaltPlusSecret);

            return enc.encodeToString(md.digest(password.getBytes())) + "$" + enc.encodeToString(SALT);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String passwordHash(String password, String salt)  {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            String saltPlusSecret = salt + Base64.getEncoder()
                    .encodeToString(getSecret()
                            .getProperty("passwordSecret").getBytes());

            byte[] hashedSaltPlusSecret = md.digest(saltPlusSecret.getBytes());
            md.update(hashedSaltPlusSecret);
            return Base64.getEncoder().encodeToString(md.digest(password.getBytes()));

        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static boolean checkPassword(String password, String passwordStored) {
        String[] hashAndSalt = passwordStored.split("\\$");
        String salt = hashAndSalt[1];
        return passwordHash(password, salt).equals(hashAndSalt[0]);
    }

    private static Properties getSecret() {
        try (InputStream is = PasswordUtil.class.getResourceAsStream("/passwordSecret.properties")) {
            Properties properties = new Properties();
            properties.load(is);

            return properties;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
