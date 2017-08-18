package pl.gisexpert.service;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.ini4j.Wini;
import org.slf4j.Logger;
import pl.gisexpert.cms.data.AccountRepository;
@ApplicationScoped
public class MailService {

	private String host;
	private String port;
	private String starttls;
	private String auth;
	private String username;
	private String password;
	private String from;

	private Properties sessionProps;

	@Inject
	Logger log;
	@Inject
	private AccountRepository accountRepository;
	@PostConstruct
	public void init() {

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
			log.error(null, ex);
		}

		sessionProps = new Properties();
		sessionProps.put("mail.smtp.auth", auth);
		sessionProps.put("mail.smtp.starttls.enable", starttls);
		sessionProps.put("mail.smtp.host", host);
		sessionProps.put("mail.smtp.port", port);
		sessionProps.put("mail.smtp.ssl.trust", host);

	}


	public void sendMail(String subject, String text, List<String> addresses) {//

		Session session = Session.getInstance(sessionProps, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		addresses
				.stream()
				.forEach(address->{try {
					Message message = new MimeMessage(session);
					message.setFrom(new InternetAddress(from));
					message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(address));
			message.setSubject(subject);
			message.setText(text);


			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}});
	}

	public void sendMail(String subject, String text, String address) throws MessagingException {//

		Session session = Session.getInstance(sessionProps, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(address));
		message.setSubject(subject);
		message.setText(text);
		Transport.send(message);
	}

}
