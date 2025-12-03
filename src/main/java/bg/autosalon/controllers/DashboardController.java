package bg.autosalon.controllers;

import bg.autosalon.utils.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;

public class DashboardController {

    @FXML
    private AnchorPane contentPane;

    @FXML
    public void initialize() {
        loadView("cars_list.fxml"); // по избор, може да е празно
    }

    private void loadView(String fxml) {
        Parent view = SceneLoader.loadNode(fxml);
        if (view != null) {
            contentPane.getChildren().setAll(view);
        }
    }

    public void openCars() { loadView("cars_list.fxml"); }
    public void openClients() { loadView("clients_list.fxml"); }
    public void openEmployees() { loadView("employees_list.fxml"); }
    public void openSales() { loadView("sales_list.fxml"); }
    public void openService() { loadView("service_list.fxml"); }

    public void logout() {
        SceneLoader.openScene("login.fxml");
    }
}
