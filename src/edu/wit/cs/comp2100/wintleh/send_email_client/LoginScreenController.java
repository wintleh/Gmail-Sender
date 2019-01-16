package edu.wit.cs.comp2100.wintleh.send_email_client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginScreenController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text emailErrorText;

    @FXML
    private Button loginButton;

    @FXML
    private Rectangle emailErrorBackground;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void login() {
    	
    	String emailAddress = emailField.getText();
    	String password = passwordField.getText();
    	
    	
    	// Added to make testing easier, uses a junk email
    	if(emailField.getText().equals("admin") && passwordField.getText().equals("admin")) {
    		emailAddress = "wintlehthrowaway@gmail.com";
    		password = "BadPassword1";
    	}
    	
    	
    	boolean threwException = false;

    	// Attempt to make a new EmailSender object and save it in EmailClient
    	try {
    		EmailClient.setSender(new EmailSender(emailAddress, password));
    	}
    	catch(IllegalArgumentException e) {
    		threwException = true;
    	}
    	
    	// If the email is invalid, make the error text and background visible
    	if(threwException) {
    		emailErrorText.setVisible(true);
    		emailErrorBackground.setVisible(true);
    	}
    	// If the email was valid, save the user and password, open the compose email window
    	// Then close this window
    	else {
    		// Open the email window
    		try {
    			Parent root = FXMLLoader.load(getClass().getResource("ComposeEmail.fxml"));
    			
    			Stage stage = new Stage();
    			stage.setScene(new Scene(root));
    			stage.setTitle("Compose Mail");
    			stage.show();
    			
    			// Get the scene from the loginButton (doesn't matter what object from 
        		// the scene it is as long as its from this scene) then hide it
        		loginButton.getScene().getWindow().hide();
    		}
    		catch(Exception e) {
    			System.err.println(e.getMessage());
    			System.exit(0);
    		}
    	}
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    private void initialize() {
        assert loginButton != null : "fx:id=\"loginButton\" was not injected: check your FXML file 'LoginScreen.fxml'.";
        assert emailField != null : "fx:id=\"emailField\" was not injected: check your FXML file 'LoginScreen.fxml'.";
        assert passwordField != null : "fx:id=\"passwordField\" was not injected: check your FXML file 'LoginScreen.fxml'.";

    }
}
