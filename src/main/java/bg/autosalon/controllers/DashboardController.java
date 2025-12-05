package bg.autosalon.controllers;

import bg.autosalon.entities.User;
import bg.autosalon.enums.UserRole;
import bg.autosalon.utils.SceneLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class DashboardController {

    private User loggedUser;

    @FXML private Label userLabel;
    @FXML private StackPane contentPane;


    @FXML private Button btnCars;
    @FXML private Button btnClients;
    @FXML private Button btnEmployees;
    @FXML private Button btnSales;
    @FXML private Button btnService;

    public void setUser(User user) {
        this.loggedUser = user;
        if (userLabel != null) {
            String roleName = translateRole(user.getRole());
            userLabel.setText("Welcome,\n" + user.getFirstName() + " (" + roleName + ")");
        }


        applyRolePermissions();


        openCars();
    }

    private void applyRolePermissions() {
        if (loggedUser == null) return;



        if (loggedUser.getRole() == UserRole.CLIENT) {
            hideButton(btnClients);
            hideButton(btnEmployees);
            hideButton(btnSales);

        }
        else if (loggedUser.getRole() == UserRole.SELLER) {
            hideButton(btnEmployees);
        }

    }


    private void hideButton(Button btn) {
        btn.setVisible(false);
        btn.setManaged(false);
    }

    private String translateRole(UserRole role) {
        return switch (role) {
            case ADMIN -> "Administrator";
            case SELLER -> "Employee";
            case CLIENT -> "Client";
        };
    }



    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bg/autosalon/views/" + fxmlFile));
            Parent view = loader.load();

            contentPane.getChildren().clear();
            contentPane.getChildren().add(view);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading: " + fxmlFile);
        }
    }

    @FXML public void openCars() { loadView("cars_list.fxml"); }
    @FXML public void openClients() { loadView("clients_list.fxml"); }
    @FXML public void openEmployees() { loadView("employee_list.fxml"); }
    @FXML public void openSales() { loadView("sales_list.fxml"); }
    @FXML public void openService() { loadView("service_list.fxml"); }

    @FXML
    public void logout() {
        SceneLoader.openScene("login.fxml");
    }
}