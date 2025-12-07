package bg.autosalon.controllers;

import bg.autosalon.entities.Car;
import bg.autosalon.enums.CarStatus;
import bg.autosalon.enums.FuelType;
import bg.autosalon.services.CarService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCarController {

    @FXML private TextField brandField;
    @FXML private TextField modelField;
    @FXML private TextField vinField;
    @FXML private TextField yearField;
    @FXML private TextField mileageField;
    @FXML private TextField priceField;
    @FXML private ComboBox<FuelType> fuelComboBox;
    @FXML private Label errorLabel;

    private final CarService carService = new CarService();

    @FXML
    public void initialize() {
        fuelComboBox.getItems().setAll(FuelType.values());
    }

    @FXML
    public void onSave() {
        try {
            String brand = brandField.getText().trim();
            String model = modelField.getText().trim();
            String vin = vinField.getText().trim();

            if (brand.isEmpty() || model.isEmpty() || vin.isEmpty()) {
                errorLabel.setText("Brand, Model, and VIN are required!");
                return;
            }

            if (fuelComboBox.getValue() == null) {
                errorLabel.setText("Please select a fuel type!");
                return;
            }

            int year = Integer.parseInt(yearField.getText().trim());
            int mileage = Integer.parseInt(mileageField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());
            FuelType fuel = fuelComboBox.getValue();

            Car car = new Car();
            car.setBrand(brand);
            car.setModel(model);
            car.setVin(vin);
            car.setYear(year);
            car.setMileage(mileage);
            car.setPrice(price);
            car.setFuel(fuel);

            car.setStatus(CarStatus.AVAILABLE);

            carService.addCar(car);

            closeWindow();

        } catch (NumberFormatException e) {
            errorLabel.setText("Year, Mileage, and Price must be valid numbers!");
        } catch (Exception e) {
            errorLabel.setText("Error saving: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) brandField.getScene().getWindow();
        stage.close();
    }
}