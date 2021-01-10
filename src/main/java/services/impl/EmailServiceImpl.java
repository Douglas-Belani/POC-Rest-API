package services.impl;

import services.IEmailService;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailServiceImpl implements IEmailService {

    @Override
    public void sendEmail(String to, String subject, String body) {
        Properties properties = getEmailProperties();
        Session session = Session.getInstance(properties);
        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(properties.getProperty("mail.smtp.user")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport transport = session.getTransport("smtp");
            transport.connect(properties.getProperty("mail.smtp.host"),
                    properties.getProperty("mail.smtp.user"),
                    properties.getProperty("mail.smtp.password"));
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        } catch (AddressException e) {
            e.printStackTrace();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private Properties getEmailProperties() {
        try (InputStream is = EmailServiceImpl.class.getResourceAsStream("/email.properties")) {
            Properties properties = new Properties();
            properties.load(is);

            return properties;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
