package bg.autosalon.controllers;

import bg.autosalon.entities.Client;
import bg.autosalon.enums.UserRole;
import bg.autosalon.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddClientController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final UserService userService = new UserService();

    @FXML
    public void onSave() {
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            errorLabel.setText("Email and password are required!");
            return;
        }


        Client client = new Client();
        client.setFirstName(firstNameField.getText());
        client.setLastName(lastNameField.getText());
        client.setEmail(emailField.getText());
        client.setPhone(phoneField.getText());
        client.setPassword(passwordField.getText());
        client.setRole(UserRole.CLIENT);
        client.setLoyaltyPoints(0);

        boolean success = userService.register(client);

        if (success) {
            closeWindow();
        } else {
            errorLabel.setText("This email already exist!");
        }
    }

    @FXML
    public void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.close();
    }
}