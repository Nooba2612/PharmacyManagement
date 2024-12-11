package pharmacy.utils;

import javax.mail.*;
import javax.mail.internet.*;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.*;
import java.security.SecureRandom;

public class SendOTP {
	
	public static final Dotenv dotenv = Dotenv.load();
	public static final String FROM_EMAIL = dotenv.get("FROM_EMAIL");
	public static final String FROM_EMAIL_PASSWORD = dotenv.get("FROM_EMAIL_PASSWORD");
	
    public static String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt(999999); 
        return String.format("%06d", otp); 
    }

    public static void sendEmail(String toEmail, String otp) {
        String host = "smtp.gmail.com";
        String port = "587"; 
        String fromEmail = FROM_EMAIL;
        String fromPassword = FROM_EMAIL_PASSWORD; 

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); 

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, fromPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Mã OTP của bạn");

            String emailContent = "<h1>Mã OTP để khôi phục mật khẩu của bạn là: <strong>" + otp + "</strong></h1>";
            message.setContent(emailContent, "text/html; charset=UTF-8");

            Transport.send(message);

            System.out.println("Otp sent to email: " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
