package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StateUtil {

    public static boolean checkInitials(String stateInitials) {
        Matcher matcher = Pattern.compile("^(A[CLM]|BA|CE|ES|GO|M[ATSG]|P[ABREI]|R[JNSOR]|S[CPE]|TO|DF)$")
                .matcher(stateInitials);

        return matcher.find();
    }

}
