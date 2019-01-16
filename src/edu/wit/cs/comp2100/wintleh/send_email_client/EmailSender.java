package edu.wit.cs.comp2100.wintleh.send_email_client;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {
	
	private Session session;
	private Boolean initialized = false;
	
	public EmailSender(String username, String password) throws IllegalArgumentException{
		
		// Doing this check speeds up the error being found and doesn't put any strain on the gmail mail server
		// Checks if the username or password is blank
		if(username.equals("") || password.equals("")) {
			throw new IllegalArgumentException("Username or password is blank");
		}
		
		// Will throw an exception if the username is not valid
		checkGmailAddress(username);
		
		// Host is gmail
		String host = "smtp.gmail.com";
			
		// Create new properties object, add all necessary properties
		Properties properties = System.getProperties();
		
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.user", username); // From
		properties.put("mail.smtp.password", password);
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
			
		// Create a session based on the properties, save in the private data field
		session = Session.getDefaultInstance(properties);
		
		// Test if it can login
		// Throw IllegalArgumentException if it fails to login
		if(!testLogin()) {
			throw new IllegalArgumentException("Could not connect based on given username and password");
		}
	
		// Passed everything, it is initialized
		initialized = true;
	}
	
	/**
	 * Tests if the email and password combo is valid
	 * 
	 * @return True if the combo is valid, False if it is not
	 */
	public Boolean testLogin(){

		// Attempt to connect with the session object
		// Tests the username and password
		try {
			
			Transport transport = session.getTransport("smtp");
			transport.connect(session.getProperty("mail.smtp.host"), session.getProperty("mail.smtp.user"), session.getProperty("mail.smtp.password"));
			transport.close();	// Close immediately because it was only being used to test the connection
		} 
		catch(MessagingException e) { // MessagingException thrown if login is unsuccessful
			return false;
		}
		
		return true;
	}
	
	/**
	 * Precondition - Must be initialized (Login credentials are checked during initialization)
	 * 
	 * @param to
	 * @param subject
	 * @param body
	 * @throws AssertionError
	 * @throws MessagingException 
	 * @throws AddressException 
	 */
	public void send(String[] to, String subject, String body) throws AssertionError, AddressException, MessagingException{
		
		// Double check that the object is initialized and logged in
		assert initialized;
				
		Message message = new MimeMessage(session);
		String host = session.getProperty("mail.smtp.host");
		String from = session.getProperty("mail.smtp.user");

		message.setFrom(new InternetAddress(from));
		InternetAddress[] toAddress = new InternetAddress[to.length];
			
		for(int addressIdx = 0; addressIdx < toAddress.length; addressIdx++) {
			toAddress[addressIdx] = new InternetAddress(to[addressIdx]);
		}
			
		message.setRecipients(Message.RecipientType.TO, toAddress);
		message.setSubject(subject);
		message.setText(body);
			
		Transport transport = session.getTransport("smtp");
		transport.connect(host, from, session.getProperty("mail.smtp.password"));
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();

	}
	
	/**
	 * Checks that valid email was specified. Throws an exception if it was not valid
	 * 
	 * @param emailAddress 
	 * @throws IllegalArgumentException
	 */
	private static void checkGmailAddress(String emailAddress) {
		
		if(!(isValidGmail(emailAddress))) {
			throw new IllegalArgumentException("Invalid gmail address");
		}
	}
	
	/**
	 * Checks if an email is valid. 
	 * Must be gmail email
	 * 
	 * @param gmailAddress A gmail address
	 * @return True if the email follows before stated guidelines, False if it does not.
	 */
	public static boolean isValidGmail(String gmailAddress) {
		
		// Length must be at least 11 characters (A@gmail.com)
		// Also must not have any spaces
		// And should be a gmail address
		if(	(gmailAddress.length() <= 11) 	||
			(gmailAddress.contains(" "))	||
			!(gmailAddress.endsWith("@gmail.com"))
			) {
			return false;
		}
		
		// All criteria met, gmailAddress is a valid gmail address
		return true;
	}
	
	/**
	 * Checks if an email is valid.
	 * Must be a .com or .edu email address
	 * 
	 * @param emailAddress An email address
	 * @return True if the email follows before stated guidelines, False if it does not.
	 */
	public static boolean isValidEmail(String emailAddress) {
		
		Boolean validSuffix = false;
		
		if( emailAddress.endsWith(".com")	||
			emailAddress.endsWith(".edu")
			) {
			validSuffix = true;
		}
		
		// Length must be at least 7 characters (A@A.com)
		// Must not have any spaces or commas
		// Should be a .edu or .com address
		// Must contain an @
		if(	emailAddress.length() <= 7 		||
			emailAddress.contains(" ")		||
			emailAddress.contains(",")		||
			!validSuffix					||
			!(emailAddress.contains("@"))
			) {
			return false;
		}
		
		// All criteria met, emailAddress is a valid email address
		return true;
	}

}
