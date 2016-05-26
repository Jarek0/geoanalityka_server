package pl.gisexpert.util;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.ini4j.Wini;

public class SendMail {

    private String host;
    private String port;
    private String starttls;
    private String auth;
    private String username;
    private String password;
    private String from;

    private final Session session;

    public SendMail() {

        Wini config;
        try {
            config = new Wini(getClass().getResource("/mail.ini"));
            host = config.get("config", "host", String.class);
            port = config.get("config", "port", String.class);
            starttls = config.get("config", "starttls", String.class);
            auth = config.get("config", "auth", String.class);
            username = config.get("config", "username", String.class);
            password = config.get("config", "password", String.class);
            from = config.get("config", "from", String.class);
            
        } catch (IOException ex) {
            Logger.getLogger(SendMail.class.getName()).log(Level.SEVERE, null, ex);
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    public void sendMail(String subject, String text, String address) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(address));
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
