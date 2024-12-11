package pharmacy.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    public static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private PasswordUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }
    
}
