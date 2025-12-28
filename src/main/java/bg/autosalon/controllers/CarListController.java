package bg.autosalon.controllers;

import bg.autosalon.entities.Car;
import bg.autosalon.services.CarService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
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

    private final CarService carService = new CarService();
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

    @FXML
    public void onAddCar() {
        openCarForm(null);
    }


    @FXML
    public void onEditCar() {
        Car selected = carsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a car to edit!");
            return;
        }
        openCarForm(selected);
    }

    @FXML
    public void onDeleteCar() {
        Car selected = carsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a car to delete!");
            return;
        }

        carService.deleteCar(selected.getId());
        loadCars();
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
            stage.setTitle(carToEdit == null ? "Add Car" : "Edit Car");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadCars();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setContentText(message);
        alert.showAndWait();
    }
}