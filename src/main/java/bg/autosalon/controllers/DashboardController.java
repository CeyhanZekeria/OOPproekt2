package bg.autosalon.controllers;

import bg.autosalon.entities.Sale;
import bg.autosalon.entities.ServiceRecord;
import bg.autosalon.entities.User;
import bg.autosalon.enums.CarStatus;
import bg.autosalon.enums.UserRole;
import bg.autosalon.services.CarService;
import bg.autosalon.services.ClientService;
import bg.autosalon.services.SaleService;
import bg.autosalon.services.ServiceRecordService;
import bg.autosalon.utils.SceneLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class DashboardController {

    private User loggedUser;

    @FXML
    private Label userLabel;
    @FXML
    private StackPane contentPane;
    @FXML
    private VBox dashboardHome;

    @FXML
    private Label totalCarsLabel;
    @FXML
    private Label totalSalesLabel;
    @FXML
    private Label totalClientsLabel;

    @FXML
    private VBox revenueCard;
    @FXML
    private VBox clientsCard;
    @FXML
    private VBox quickActionsBox;

    @FXML
    private Button btnCars;
    @FXML
    private Button btnClients;
    @FXML
    private Button btnEmployees;
    @FXML
    private Button btnSales;
    @FXML
    private Button btnService;
    @FXML
    private Button btnRequests;
    @FXML
    private Button btnNotifications;
    @FXML
    private Button btnReport;

    private final CarService carService = new CarService();
    private final SaleService saleService = new SaleService();
    private final ClientService clientService = new ClientService();
    private final ServiceRecordService serviceRecordService = new ServiceRecordService();

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


            double carRevenue = saleService.getAllSales().stream()
                    .mapToDouble(Sale::getFinalPrice)
                    .sum();


            double serviceRevenue = serviceRecordService.getAllRecords().stream()
                    .mapToDouble(ServiceRecord::getPrice)
                    .sum();


            double totalRevenue = carRevenue + serviceRevenue;
            totalSalesLabel.setText(String.format("%.0f BGN", totalRevenue));


            String tooltipText = String.format("Sales: %.2f BGN\nService: %.2f BGN", carRevenue, serviceRevenue);
            Tooltip breakdown = new Tooltip(tooltipText);
            breakdown.setShowDelay(Duration.ZERO);
            totalSalesLabel.setTooltip(breakdown);


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
            hideElement(btnRequests);
            hideElement(revenueCard);
            hideElement(clientsCard);
            hideElement(quickActionsBox);
            hideElement(btnReport);
        } else {
            hideElement(btnNotifications);
            if (loggedUser.getRole() == UserRole.SELLER) {
                hideElement(btnEmployees);
            }
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


    @FXML
    public void onGenerateReport() {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Save Financial Report");
        fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("Autosalon_Report_" + java.time.LocalDate.now() + ".pdf");

        java.io.File file = fileChooser.showSaveDialog(totalSalesLabel.getScene().getWindow());

        if (file != null) {
            try {
                bg.autosalon.utils.PdfReportGenerator generator = new bg.autosalon.utils.PdfReportGenerator();
                generator.generateFinancialReport(file);

                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Report Generated");
                alert.setHeaderText(null);
                alert.setContentText("PDF Report saved successfully!\nPath: " + file.getAbsolutePath());
                alert.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Could not generate report: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }


    @FXML
    public void openCars() {
        loadView("cars_list.fxml");
    }

    @FXML
    public void openClients() {
        loadView("clients_list.fxml");
    }

    @FXML
    public void openEmployees() {
        loadView("employees_list.fxml");
    }

    @FXML
    public void openSales() {
        loadView("sales_list.fxml");
    }

    @FXML
    public void openService() {
        loadView("service_list.fxml");
    }

    @FXML
    public void openRequests() {
        loadView("request_list.fxml");
    }

    @FXML
    public void openNotifications() {
        loadView("my_notifications.fxml");
    }

    @FXML
    public void logout() {
        bg.autosalon.utils.SessionManager.logout();
        SceneLoader.openScene("login.fxml");
    }
}