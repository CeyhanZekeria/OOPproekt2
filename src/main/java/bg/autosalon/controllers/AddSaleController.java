package bg.autosalon.controllers;

import bg.autosalon.entities.*;
import bg.autosalon.enums.CarStatus;
import bg.autosalon.services.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AddSaleController {

    @FXML private ComboBox<Car> carCombo;
    @FXML private ComboBox<Client> clientCombo;
    @FXML private ComboBox<Employee> employeeCombo;
    @FXML private DatePicker datePicker;
    @FXML private TextField priceField;
    @FXML private Label errorLabel;

    private final CarService carService = new CarService();
    private final ClientService clientService = new ClientService();
    private final EmployeeService employeeService = new EmployeeService();
    private final SaleService saleService = new SaleService();

    @FXML
    public void initialize() {

        List<Car> availableCars = carService.getAllCars().stream()
                .filter(c -> c.getStatus() == CarStatus.AVAILABLE)
                .collect(Collectors.toList());
        carCombo.setItems(FXCollections.observableArrayList(availableCars));


        clientCombo.setItems(FXCollections.observableArrayList(clientService.getAllClients()));
        employeeCombo.setItems(FXCollections.observableArrayList(employeeService.getAllEmployees()));

        datePicker.setValue(LocalDate.now());


        setupComboDisplay();
    }

    @FXML
    public void onSave() {
        try {
            if (carCombo.getValue() == null || clientCombo.getValue() == null || employeeCombo.getValue() == null) {
                errorLabel.setText("Всички полета са задължителни!");
                return;
            }


            Sale sale = new Sale();
            sale.setCar(carCombo.getValue());
            sale.setClient(clientCombo.getValue());
            sale.setEmployee(employeeCombo.getValue());
            sale.setSaleDate(datePicker.getValue());
            sale.setFinalPrice(Double.parseDouble(priceField.getText()));

            saleService.addSale(sale);

            Car soldCar = carCombo.getValue();
            soldCar.setStatus(CarStatus.SOLD);
            carService.updateCar(soldCar);

            closeWindow();
        } catch (Exception e) {
            errorLabel.setText("Грешка: " + e.getMessage());
        }
    }

    private void setupComboDisplay() {
        carCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Car c) { return c == null ? "" : c.getBrand() + " " + c.getModel() + " (" + c.getVin() + ")"; }
            @Override public Car fromString(String s) { return null; }
        });

        clientCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Client c) { return c == null ? "" : c.getFirstName() + " " + c.getLastName(); }
            @Override public Client fromString(String s) { return null; }
        });

        employeeCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Employee e) { return e == null ? "" : e.getFirstName() + " " + e.getLastName(); }
            @Override public Employee fromString(String s) { return null; }
        });
    }

    @FXML public void onCancel() { closeWindow(); }
    private void closeWindow() { ((Stage) priceField.getScene().getWindow()).close(); }
}