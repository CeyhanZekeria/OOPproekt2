package bg.autosalon.controllers;

import bg.autosalon.entities.Car;
import bg.autosalon.entities.Client;
import bg.autosalon.entities.TestDriveRequest;
import bg.autosalon.entities.User;
import bg.autosalon.enums.UserRole;
import bg.autosalon.services.CarService;
import bg.autosalon.services.RequestService;
import bg.autosalon.utils.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.stream.Collectors;

public class CarListController {

    @FXML private TextField searchField;
    @FXML private TableView<Car> carsTable;

    @FXML private TableColumn<Car, String> colBrand;
    @FXML private TableColumn<Car, String> colModel;
    @FXML private TableColumn<Car, String> colVin;
    @FXML private TableColumn<Car, String> colYear;
    @FXML private TableColumn<Car, String> colFuel;
    @FXML private TableColumn<Car, String> colPrice;
    @FXML private TableColumn<Car, String> colStatus;


    @FXML private Button btnAdd;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;
    @FXML private Button btnRequest;
    @FXML private Button btnHistory;

    private final CarService carService = new CarService();
    private final RequestService requestService = new RequestService();

    private ObservableList<Car> allCars;

    @FXML
    public void initialize() {

        colBrand.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBrand()));
        colModel.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getModel()));
        colVin.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getVin()));
        colYear.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getYear())));
        colFuel.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFuel().toString()));
        colPrice.setCellValueFactory(cell -> new SimpleStringProperty(String.format("%.2f", cell.getValue().getPrice())));
        colStatus.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus().toString()));

        loadCars();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterCars(newValue));


        checkPermissions();
    }

    private void checkPermissions() {
        User currentUser = SessionManager.getCurrentUser();


        setButtonVisible(btnAdd, false);
        setButtonVisible(btnEdit, false);
        setButtonVisible(btnDelete, false);
        setButtonVisible(btnRequest, false);
        setButtonVisible(btnHistory, false);

        if (currentUser != null) {
            if (currentUser.getRole() == UserRole.CLIENT) {

                setButtonVisible(btnRequest, true);
            } else {

                setButtonVisible(btnAdd, true);
                setButtonVisible(btnEdit, true);
                setButtonVisible(btnDelete, true);
                setButtonVisible(btnHistory, true);
            }
        }
    }

    private void setButtonVisible(Button btn, boolean visible) {
        if (btn != null) {
            btn.setVisible(visible);
            btn.setManaged(visible);
        }
    }


    @FXML
    public void onRequestTestDrive() {
        Car selectedCar = carsTable.getSelectionModel().getSelectedItem();
        if (selectedCar == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a car first!");
            return;
        }

        User currentUser = SessionManager.getCurrentUser();
        if (!(currentUser instanceof Client)) {
            showAlert(Alert.AlertType.ERROR, "Only clients can request test drives!");
            return;
        }

        Dialog<LocalDate> dialog = new Dialog<>();
        dialog.setTitle("Schedule Test Drive");
        dialog.setHeaderText("Request test drive for: " + selectedCar.getBrand());

        ButtonType typeOk = new ButtonType("Send Request", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(typeOk, ButtonType.CANCEL);

        DatePicker datePicker = new DatePicker(LocalDate.now().plusDays(1));
        VBox content = new VBox(10, new Label("Select Date:"), datePicker);
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == typeOk) return datePicker.getValue();
            return null;
        });

        Optional<LocalDate> result = dialog.showAndWait();
        result.ifPresent(date -> {
            try {
                TestDriveRequest request = new TestDriveRequest();
                request.setCar(selectedCar);
                request.setClient((Client) currentUser);
                request.setRequestDate(LocalDateTime.of(date, LocalTime.of(10, 0)));

                requestService.createRequest(request);

                showAlert(Alert.AlertType.INFORMATION, "Request sent! An employee will review it.");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
            }
        });
    }


    @FXML
    public void onHistory() {
        Car selectedCar = carsTable.getSelectionModel().getSelectedItem();
        if (selectedCar == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a car first!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bg/autosalon/views/car_history.fxml"));
            Parent root = loader.load();

            CarHistoryController controller = loader.getController();
            controller.setCar(selectedCar);

            Stage stage = new Stage();
            stage.setTitle("Vehicle History Report");
            stage.setScene(new Scene(root, 600, 500));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Could not load history view.");
        }
    }


    private void loadCars() {
        allCars = FXCollections.observableArrayList(carService.getAllCars());
        carsTable.setItems(allCars);
    }

    private void filterCars(String query) {
        if (query == null || query.isEmpty()) {
            carsTable.setItems(allCars);
            return;
        }
        String lowerQuery = query.toLowerCase();
        var filtered = allCars.stream()
                .filter(c -> c.getBrand().toLowerCase().contains(lowerQuery) ||
                        c.getModel().toLowerCase().contains(lowerQuery) ||
                        c.getVin().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
        carsTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML public void onAddCar() { openCarForm(null); }

    @FXML public void onEditCar() {
        Car selected = carsTable.getSelectionModel().getSelectedItem();
        if (selected != null) openCarForm(selected);
    }

    @FXML public void onDeleteCar() {
        Car selected = carsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            carService.deleteCar(selected.getId());
            loadCars();
        }
    }

    private void openCarForm(Car carToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bg/autosalon/views/add_car.fxml"));
            Parent root = loader.load();
            if (carToEdit != null) {
                AddCarController controller = loader.getController();
                controller.setCarToEdit(carToEdit);
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
            loadCars();
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}