package pharmacy.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
	private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	private PasswordUtil() {
		throw new UnsupportedOperationException("Utility class cannot be instantiated");
	}

	public static String hashPassword(String plainPassword) {
		return passwordEncoder.encode(plainPassword);
	}

	public static boolean checkPassword(String plainPassword, String hashedPassword) {
		return passwordEncoder.matches(plainPassword, hashedPassword);
	}
}