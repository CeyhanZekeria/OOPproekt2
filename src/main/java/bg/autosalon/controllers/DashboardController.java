package bg.autosalon.controllers;

import bg.autosalon.entities.Sale;
import bg.autosalon.entities.User;
import bg.autosalon.enums.CarStatus;
import bg.autosalon.enums.UserRole;
import bg.autosalon.services.CarService;
import bg.autosalon.services.ClientService;
import bg.autosalon.services.SaleService;
import bg.autosalon.utils.SceneLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class DashboardController {

    private User loggedUser;

    @FXML private Label userLabel;
    @FXML private StackPane contentPane;
    @FXML private VBox dashboardHome;

    @FXML private Label totalCarsLabel;
    @FXML private Label totalSalesLabel;
    @FXML private Label totalClientsLabel;

    @FXML private VBox revenueCard;
    @FXML private VBox clientsCard;
    @FXML private VBox quickActionsBox;

    @FXML private Button btnCars;
    @FXML private Button btnClients;
    @FXML private Button btnEmployees;
    @FXML private Button btnSales;
    @FXML private Button btnService;

    private final CarService carService = new CarService();
    private final SaleService saleService = new SaleService();
    private final ClientService clientService = new ClientService();

    public void setUser(User user) {
        this.loggedUser = user;

        if (userLabel != null) {
            String roleName = translateRole(user.getRole());
            userLabel.setText("Welcome,\n" + user.getFirstName() + " (" + roleName + ")");
        }

        applyRolePermissions();
        refreshStats();
    }

    private void refreshStats() {
        long availableCars = carService.getAllCars().stream()
                .filter(c -> c.getStatus() == CarStatus.AVAILABLE)
                .count();
        totalCarsLabel.setText(String.valueOf(availableCars));

        if (loggedUser.getRole() != UserRole.CLIENT) {
            double totalRevenue = saleService.getAllSales().stream()
                    .mapToDouble(Sale::getFinalPrice)
                    .sum();
            totalSalesLabel.setText(String.format("%.0f BGN", totalRevenue));

            int totalClients = clientService.getAllClients().size();
            totalClientsLabel.setText(String.valueOf(totalClients));
        }
    }

    private void applyRolePermissions() {
        if (loggedUser == null) return;

        if (loggedUser.getRole() == UserRole.CLIENT) {
            hideElement(btnClients);
            hideElement(btnEmployees);
            hideElement(btnSales);

            hideElement(revenueCard);
            hideElement(clientsCard);

            hideElement(quickActionsBox);

        } else if (loggedUser.getRole() == UserRole.SELLER) {
            hideElement(btnEmployees);
        }
    }

    private void hideElement(Node node) {
        if (node != null) {
            node.setVisible(false);
            node.setManaged(false);
        }
    }

    private String translateRole(UserRole role) {
        return switch (role) {
            case ADMIN -> "Administrator";
            case SELLER -> "Seller";
            case CLIENT -> "Client";
        };
    }

    @FXML
    public void openHome() {
        contentPane.getChildren().clear();
        if (dashboardHome != null) {
            contentPane.getChildren().add(dashboardHome);
            refreshStats();
        }
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bg/autosalon/views/" + fxmlFile));
            Parent view = loader.load();

            contentPane.getChildren().clear();
            contentPane.getChildren().add(view);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading view: " + fxmlFile);
        }
    }

    @FXML public void openCars() { loadView("cars_list.fxml"); }
    @FXML public void openClients() { loadView("clients_list.fxml"); }
    @FXML public void openEmployees() { loadView("employees_list.fxml"); }
    @FXML public void openSales() { loadView("sales_list.fxml"); }
    @FXML public void openService() { loadView("service_list.fxml"); }

    @FXML
    public void logout() {
        SceneLoader.openScene("login.fxml");
    }
}