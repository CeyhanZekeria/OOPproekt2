package bg.autosalon.controllers;

import bg.autosalon.entities.User;
import bg.autosalon.enums.UserRole;
import bg.autosalon.services.UserService;
import bg.autosalon.utils.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegisterController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField phoneField;
    @FXML private Label errorLabel;
    @FXML private Label successLabel;

    private final UserService userService = new UserService();

    @FXML
    public void onRegister() {
        User user = new User();
        user.setFirstName(firstNameField.getText());
        user.setLastName(lastNameField.getText());
        user.setEmail(emailField.getText());
        user.setPassword(passwordField.getText());
        user.setPhone(phoneField.getText());
        user.setRole(UserRole.CLIENT);

        boolean ok = userService.register(user);

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
