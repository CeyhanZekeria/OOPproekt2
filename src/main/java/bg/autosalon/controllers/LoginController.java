package bg.autosalon.controllers;

import bg.autosalon.entities.User;
import bg.autosalon.services.UserService;
import bg.autosalon.utils.SceneLoader;
import javafx.fxml.*;
import javafx.scene.control.*;


public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final UserService userService = new UserService();

    @FXML
    public void onLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        User user = userService.login(email, password);

        if (user == null) {
            errorLabel.setText("Invalid email or password!");
            return;
        }

        errorLabel.setText("");

        SceneLoader.openScene("dashboard.fxml");

    }
}
