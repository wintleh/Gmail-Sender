package edu.wit.cs.comp2100.wintleh.send_email_client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EmailClient extends Application {
	
	// Hold the EmailSender object, used by both LoginScreen and ComposeEmail
	private static EmailSender sender;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		// Show LoginScreen
		try {
			// Load the root from the fxml file
			Parent root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));

			// Create a scene with the root, add it to the stage, and show the stage
			stage.setScene(new Scene(root));
			stage.show();
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
			System.exit(0);
		}
	}
	
	/**
	 * Send the EmailSender object
	 * 
	 * @param emailSender A EmailSender object
	 */
	public static void setSender(EmailSender emailSender) {
		sender = emailSender;
	}
	
	/**
	 * Get the EmailSender object
	 * 
	 * @return The EmailSender object
	 */
	public static EmailSender getSender() {
		return sender;
	}
}
