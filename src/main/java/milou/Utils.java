package milou;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Utils {
    private static final String DOMAIN = "milou.com";
    private static final Random random = new Random();
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";

    public static String formatEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }

        if (email.contains("@")) {
            return email.toLowerCase();
        }
        return email.toLowerCase() + "@" + DOMAIN;
    }

    public static String generateRandomId(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}