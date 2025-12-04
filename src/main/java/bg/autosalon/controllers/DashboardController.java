package bg.autosalon.controllers;

import bg.autosalon.entities.User;
import bg.autosalon.enums.UserRole;
import bg.autosalon.utils.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    private User loggedUser;

    @FXML
    private Label userLabel;

    public void setUser(User user) {
        this.loggedUser = user;

        if (userLabel != null) {
            userLabel.setText("Logged as: " + user.getEmail() + " (" + user.getRole() + ")");
        }

        applyRolePermissions();
    }


    private void applyRolePermissions() {
        if (loggedUser == null) return;

        if (loggedUser.getRole() == UserRole.CLIENT) {

        }
    }



    @FXML
    public void openCars() {
        SceneLoader.openScene("cars_list.fxml");
    }

    @FXML
    public void openClients() {
        SceneLoader.openScene("clients_list.fxml");
    }

    @FXML
    public void openEmployees() {
        SceneLoader.openScene("employee_list.fxml");
    }

    @FXML
    public void openSales() {
        SceneLoader.openScene("sales_list.fxml");
    }

    @FXML
    public void openService() {
        SceneLoader.openScene("service_list.fxml");
    }

    @FXML
    public void logout() {
        SceneLoader.openScene("login.fxml");
    }
}
