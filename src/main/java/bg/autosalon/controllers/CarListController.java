package bg.autosalon.controllers;

import bg.autosalon.entities.Car;
import bg.autosalon.services.CarService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
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

    // Свързваме новото поле за търсене
    @FXML private TextField searchField;

    private final CarService carService = new CarService();

    // Списък, който пази всички данни от базата
    private ObservableList<Car> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupColumns();
        loadCars();     // Зарежда данни от базата
        setupSearch();  // Активира търсачката
    }

    private void setupColumns() {
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colVin.setCellValueFactory(new PropertyValueFactory<>("vin"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        colFuel.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getFuel() != null ? cell.getValue().getFuel().toString() : ""
        ));

        colStatus.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getStatus() != null ? cell.getValue().getStatus().toString() : ""
        ));
    }

    private void loadCars() {
        masterData.clear();
        masterData.addAll(carService.getAllCars());
    }

    private void setupSearch() {
        // 1. Създаваме филтриран списък, който гледа към masterData
        FilteredList<Car> filteredData = new FilteredList<>(masterData, p -> true);

        // 2. Слушаме за промени в полето за писане
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(car -> {
                // Ако полето е празно, показваме всичко
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Проверяваме дали текстът съвпада с Марка, Модел или VIN
                if (car.getBrand().toLowerCase().contains(lowerCaseFilter)) return true;
                if (car.getModel().toLowerCase().contains(lowerCaseFilter)) return true;
                if (car.getVin().toLowerCase().contains(lowerCaseFilter)) return true;
                if (String.valueOf(car.getYear()).contains(lowerCaseFilter)) return true;

                return false; // Няма съвпадение -> скрий реда
            });
        });

        // 3. Опаковаме във SortedList, за да работи и сортирането при клик на колона
        SortedList<Car> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(carsTable.comparatorProperty());

        // 4. Слагаме финалния списък в таблицата
        carsTable.setItems(sortedData);
    }

    @FXML
    public void onAddCar() {
        openModal("/bg/autosalon/views/add_car.fxml", "Добавяне на автомобил");
    }

    // Помощен метод за отваряне на прозорци
    private void openModal(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            loadCars(); // Обновяваме след затваряне
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onDeleteCar() {
        Car selected = carsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Внимание");
            alert.setContentText("Моля изберете автомобил!");
            alert.showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Изтриване");
        confirm.setHeaderText("Ще изтриете: " + selected.getBrand() + " " + selected.getModel());

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            carService.deleteCar(selected.getId());
            loadCars();
        }
    }
}