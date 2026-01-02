package bg.autosalon.controllers;

import bg.autosalon.entities.User;
import bg.autosalon.services.UserService;
import bg.autosalon.utils.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

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

        bg.autosalon.utils.SessionManager.login(user);

        var controller = SceneLoader.openSceneWithController("dashboard.fxml");

        if (controller instanceof DashboardController dashboardController) {
            dashboardController.setUser(user);
        }

    }

    @FXML
    public void openRegister() {
        SceneLoader.openScene("register.fxml");
    }
}
