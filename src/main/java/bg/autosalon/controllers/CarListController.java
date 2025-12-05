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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CarListController {

    @FXML private TableView<Car> carsTable;
    @FXML private TableColumn<Car, String> colBrand;
    @FXML private TableColumn<Car, String> colModel;
    @FXML private TableColumn<Car, String> colVin;
    @FXML private TableColumn<Car, Integer> colYear;
    @FXML private TableColumn<Car, String> colFuel;
    @FXML private TableColumn<Car, Double> colPrice;
    @FXML private TableColumn<Car, String> colStatus;

    private final CarService carService = new CarService();

    @FXML
    public void initialize() {
        setupColumns();
        loadCars();
    }

    private void setupColumns() {

        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colVin.setCellValueFactory(new PropertyValueFactory<>("vin"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));


        colFuel.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getFuel() != null ? cell.getValue().getFuel().toString() : "N/A"
        ));

        colStatus.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getStatus() != null ? cell.getValue().getStatus().toString() : "N/A"
        ));
    }

    private void loadCars() {

        List<Car> cars = carService.getAllCars();
        ObservableList<Car> observableCars = FXCollections.observableArrayList(cars);
        carsTable.setItems(observableCars);
    }

    @FXML
    public void onAddCar() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bg/autosalon/views/add_car.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("Add vehicle");
            stage.setScene(new Scene(root));


            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();


            loadCars();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "This form cannot load: " + e.getMessage());
        }
    }

    @FXML
    public void onDeleteCar() {

        Car selectedCar = carsTable.getSelectionModel().getSelectedItem();

        if (selectedCar == null) {
            showAlert("Attention", "Please, chose vehicle.");
            return;
        }


        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation!");
        confirm.setHeaderText("Are you sure?");
        confirm.setContentText(selectedCar.getBrand() + " " + selectedCar.getModel() + " (" + selectedCar.getVin() + ")");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            carService.deleteCar(selectedCar.getId());

            loadCars();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}