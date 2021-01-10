package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserUtil {

    public static void checkCpf(String cpf) {
        Matcher cpfMatcher = Pattern.compile("^\\d{9}-?\\d{2}$").matcher(cpf);

        if (!cpfMatcher.find()) {
            throw new IllegalArgumentException("CPF must have 9 digits followed by - followed by 2 digits.");
        }
    }

    public static void checkPassword(String password) {
        Matcher passwordMatcher = Pattern
                .compile("^(?=.*[a-z]+)(?=.*[A-Z]+)(?=.*\\d+)(?=.*[!@#$%^&*()-]+).{6,20}$")
                .matcher(password);

        if (!passwordMatcher.find()) {
            throw new IllegalArgumentException("Wrong password format. Password must contain at least one upper case letter, " +
                    "one lower case letter, one number, !@#$%^&*()-+ and have 6-20 characters");
        }

    }

    public static void checkEmail(String email) {
        Matcher emailMatcher = Pattern
                .compile("^(?=[\\w.]+@(?:outlook|hotmail|gmail)\\.com).+$")
                .matcher(email);

        if (!emailMatcher.find()) {
            throw new IllegalArgumentException("Invalid email");
        }

    }
}
