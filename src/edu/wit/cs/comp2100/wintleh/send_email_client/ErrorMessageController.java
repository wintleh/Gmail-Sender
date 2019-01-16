package edu.wit.cs.comp2100.wintleh.send_email_client;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ErrorMessageController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text errorText;

    @FXML
    void initialize() {
        assert errorText != null : "fx:id=\"errorText\" was not injected: check your FXML file 'ErrorMessage.fxml'.";
        
        errorText.setText(ComposeEmailController.getEmailErrorMessage());
    }
}
