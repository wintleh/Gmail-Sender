package edu.wit.cs.comp2100.wintleh.send_email_client;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ComposeEmailController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField inputTo;

    @FXML
    private Button sendEmail;

    @FXML
    private TextField inputSubject;

    @FXML
    private TextArea inputMessage;
    
    private static String emailError;
    
    /**
     * Gets the values from the box and opens the login window
     */
    @FXML
    private void sendEmail() {
    	
    	// Gets the text from the text boxes in the GUI
    	String recipients = inputTo.getText().replaceAll(" ", ""); // Remove all the spaces from the string
    	String subject = inputSubject.getText();
    	String body = inputMessage.getText();
    	
    	Boolean exceptionDetected = false;
    	
    	// Send the email with the data from the text boxes
    	// If any exception is detected then the message has failed to send
    	try {
			EmailClient.getSender().send(getRecipientArray(recipients), subject, body);
		} catch (IOException e) {
			exceptionDetected = true;
			showErrorMessage(e.getMessage());
		} catch (AssertionError e) {
			exceptionDetected = true;
			showErrorMessage(e.getMessage());
		} catch (AddressException e) {
			exceptionDetected = true;
			showErrorMessage(e.getMessage());
		} catch (MessagingException e) {
			exceptionDetected = true;
			showErrorMessage(e.getMessage());
		}
    	
    	// If there was no exception then the email was sent successfully, close the program
    	if(!exceptionDetected) {
    		System.exit(0);
    	}
    }
    
    /**
     * Getter method to get the error message. Used by ErrorMessageController to display the error message.
     * 
     * @return String representing the error that occurred
     */
    public static String getEmailErrorMessage() {
    	return emailError;
    }
    
    
    /**
     * Opens a new window which displays the error message.
     * 
     * @param errorMessage A string representing the error that occurred
     */
    private void showErrorMessage(String errorMessage) {
    	
    	emailError = errorMessage;
    	
    	// Open the email window
		try {
			Parent root = FXMLLoader.load(getClass().getResource("ErrorMessage.fxml"));
			
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setTitle("Error");
			stage.show();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
    }
    
    /**
     * 
     * @param recipients
     * @throws IOException
     */
    private String[] getRecipientArray(String recipients) throws IOException{
    	
    	if(recipients.isEmpty()) {
    		throw new IOException("Recipients was empty");
    	}
    	
    	// Gets the recipients based on the number of commas
    	int recipientCount = getNumberOfRecipients(recipients);
    	
    	// If there is only one recipient
    	if(recipientCount == 1) {
    		
    		// If the specified email is invalid
    		if(!EmailSender.isValidEmail(recipients)) {
    			throw new IOException("Recipients contained invalid email address");
    		}
    		// Else it is valid
    		else {
    			// Create a new string array with only the one recipient, and return a copy of that array
    			String[] toArray = {recipients};
    			return Arrays.copyOf(toArray, toArray.length);
    		}
    	}
    	
    	// If there are multiple recipients	
    	
    	return arrayByCommas(recipients);
    }
    
    /**
     * Get the number of recipients based on the number of commas
     * 
     * @param recipients A string containing recipient(s)
     * @return The number of recipients (based on the number of commas)
     */
    private static int getNumberOfRecipients(String recipients) {
    	
    	// Figure out the number of recipients based on the number of commas
    	int recipientCount = 1;
    	
    	// Count the number of commas in the string
    	for(int index = 0; index < recipients.length(); index++) {
    		if(recipients.charAt(index) == ',') {
    			recipientCount++;
    		}
    	}
    	
    	// Return the number of recipients
    	return recipientCount;
    }
    
    /**
     * Assume the list is made correctly
     * 
     * @param recipients
     * @return
     */
    private static String[] arrayByCommas(String recipients) {
    	
    	ArrayList<String> recipientList = new ArrayList<>();
    	
    	int prevComma = 0;
    	
    	// Look through the entire string
    	for(int index = 0; index <= recipients.length(); index++) {
    		
    		// If the index is past the last index, add the string from prevComma to the end to the list
    		if(index == recipients.length()) {
    	    	recipientList.add(recipients.substring(prevComma));
    		}
    		// Else if the charater at index is a comma, add the string from prevComma to index to the list
    		else if(recipients.charAt(index) == ',') {
    			recipientList.add(recipients.substring(prevComma, index));

    			// ++index because it drops the comma
    			prevComma = ++index;
    		}
    	}
    
    	// Turn the ArrayList into an array and return the array
    	String[] returnArray = new String[recipientList.size()];
    	return recipientList.toArray(returnArray);
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    private void initialize() {
        assert inputTo != null : "fx:id=\"inputTo\" was not injected: check your FXML file 'composeEmail.fxml'.";
        assert sendEmail != null : "fx:id=\"sendEmail\" was not injected: check your FXML file 'composeEmail.fxml'.";
        assert inputSubject != null : "fx:id=\"inputSubject\" was not injected: check your FXML file 'composeEmail.fxml'.";
        assert inputMessage != null : "fx:id=\"inputMessage\" was not injected: check your FXML file 'composeEmail.fxml'.";

    }
}
