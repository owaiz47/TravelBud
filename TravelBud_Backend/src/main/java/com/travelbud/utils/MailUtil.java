package com.travelbud.utils;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelbud.configs.MailConfig;

@Service
public class MailUtil {

	@Autowired
	private MailConfig mailConfig;
	
	private Logger log = LoggerFactory.getLogger(MailUtil.class);

	@PostConstruct
	public void init() {
		System.out.println(mailConfig.toString());
	}

	public boolean sendMail(String receipient, String subject, String body)
			throws MessagingException, UnsupportedEncodingException {
		//String CONFIGSET = "ConfigSet";
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.port", mailConfig.getPort());
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");

		// Create a Session object to represent a mail session with the specified
		// properties.
		Session session = Session.getDefaultInstance(props);

		// Create a message with the specified information.
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(mailConfig.getSender(), mailConfig.getName()));
		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(receipient));
		msg.setSubject(subject);
		msg.setContent(body, "text/html");

		// Add a configuration set header. Comment or delete the
		// next line if you are not using a configuration set
		//msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);

		// Create a transport.
		Transport transport = session.getTransport();

		// Send the message.
		try {
			log.info("Sending mail to "+receipient);

			// Connect to Amazon SES using the SMTP username and password you specified
			// above.
			transport.connect(mailConfig.getHost(), mailConfig.getUsername(), mailConfig.getPassword());

			// Send the email.
			transport.sendMessage(msg, msg.getAllRecipients());
			log.info("Email sent!");
		} catch (Exception ex) {
			log.error("The email was not sent.");
			log.error("Error message: " + ex.getMessage());
		} finally {
			// Close and terminate the connection.
			transport.close();
		}
		return true;
	}

}
