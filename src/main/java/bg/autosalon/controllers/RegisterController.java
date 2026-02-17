package bg.autosalon.controllers;

import bg.autosalon.entities.Client;
import bg.autosalon.entities.User;
import bg.autosalon.enums.UserRole;
import bg.autosalon.services.UserService;
import bg.autosalon.utils.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegisterController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField phoneField;
    @FXML
    private Label errorLabel;
    @FXML
    private Label successLabel;

    private final UserService userService = new UserService();

    @FXML
    public void onRegister() {
        Client client = new Client();

        client.setFirstName(firstNameField.getText());
        client.setLastName(lastNameField.getText());
        client.setEmail(emailField.getText());
        client.setPassword(passwordField.getText());
        client.setPhone(phoneField.getText());


        client.setRole(UserRole.CLIENT);


        client.setLoyaltyPoints(0);


        boolean ok = userService.register(client);

        if (!ok) {
            errorLabel.setText("Email already in use!");
            successLabel.setText("");
            return;
        }

        errorLabel.setText("");
        successLabel.setText("Registration successful!");


        SceneLoader.openScene("dashboard.fxml");

    }

    @FXML
    public void goToLogin() {
        SceneLoader.openScene("login.fxml");
    }
}
