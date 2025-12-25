package in.co.rays.proj4.util;

import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import in.co.rays.proj4.exception.ApplicationException;

/**
 * EmailUtility is responsible for sending emails using JavaMail API.
 * <p>
 * Configuration (SMTP host, port, username, password) is loaded from: <br>
 * <b>in.co.rays.proj4.bundle.system</b> resource bundle.
 * <p>
 * It works together with {@link EmailMessage} and {@link EmailBuilder}.
 *
 * Example usage:
 * 
 * <pre>
 * EmailMessage msg = new EmailMessage();
 * msg.setTo("user@mail.com");
 * msg.setSubject("Test Mail");
 * msg.setMessage("&lt;h1&gt;Hello&lt;/h1&gt;");
 * msg.setMessageType(EmailMessage.HTML_MSG);
 * EmailUtility.sendMail(msg);
 * </pre>
 *
 * @author Deepak Verma
 * @version 1.0
 */
public class EmailUtility {

	/** Resource bundle containing SMTP/email configuration. */
	static ResourceBundle rb = ResourceBundle.getBundle("in.co.rays.proj4.bundle.system");

	private static final String SMTP_HOST_NAME = rb.getString("smtp.server");
	private static final String SMTP_PORT = rb.getString("smtp.port");
	private static final String EMAIL_FROM_ADDRESS = rb.getString("email.login");
	private static final String EMAIL_PASSWORD = rb.getString("email.pwd");
	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

	private static final Properties props = new Properties();

	static {
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		props.put("mail.debug", "true");
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.put("mail.smtp.socketFactory.fallback", "false");
	}

	/**
	 * Sends an email using the given {@link EmailMessage} data.
	 *
	 * @param emailMessageDTO EmailMessage DTO containing to/subject/body/type
	 * @throws ApplicationException if there is any problem sending email
	 */
	public static void sendMail(EmailMessage emailMessageDTO) throws ApplicationException {
		try {
			if (emailMessageDTO == null) {
				throw new ApplicationException("Email data is null");
			}

			// Setup mail session with authentication
			Session session = Session.getDefaultInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(EMAIL_FROM_ADDRESS, EMAIL_PASSWORD);
				}
			});

			// Create and setup the email message
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(EMAIL_FROM_ADDRESS));
			msg.setRecipients(Message.RecipientType.TO, getInternetAddresses(emailMessageDTO.getTo()));
			msg.setSubject(emailMessageDTO.getSubject());

			// Content type based on messageType
			String contentType = (emailMessageDTO.getMessageType() == EmailMessage.HTML_MSG) ? "text/html"
					: "text/plain";

			msg.setContent(emailMessageDTO.getMessage(), contentType);

			// Send the message
			Transport.send(msg);

		} catch (Exception ex) {
			throw new ApplicationException("Email Error: " + ex.getMessage(), ex);
		}
	}

	/**
	 * Converts comma-separated email string to InternetAddress array.
	 *
	 * @param emails comma separated email addresses
	 * @return array of InternetAddress
	 * @throws Exception if any email address is invalid
	 */
	private static InternetAddress[] getInternetAddresses(String emails) throws Exception {
		if (emails == null || emails.trim().isEmpty()) {
			return new InternetAddress[0];
		}
		String[] emailArray = emails.split(",");
		InternetAddress[] addresses = new InternetAddress[emailArray.length];
		for (int i = 0; i < emailArray.length; i++) {
			addresses[i] = new InternetAddress(emailArray[i].trim());
		}
		return addresses;
	}
}
