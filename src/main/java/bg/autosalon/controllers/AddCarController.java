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
        // Пълним падащото меню с видовете гориво от Enum-а
        fuelComboBox.getItems().setAll(FuelType.values());
        // Избираме първото по подразбиране (по желание)
        // fuelComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    public void onSave() {
        try {
            // 1. Проверка за празни текстови полета
            String brand = brandField.getText().trim();
            String model = modelField.getText().trim();
            String vin = vinField.getText().trim();

            if (brand.isEmpty() || model.isEmpty() || vin.isEmpty()) {
                errorLabel.setText("Марка, Модел и VIN са задължителни!");
                return;
            }

            if (fuelComboBox.getValue() == null) {
                errorLabel.setText("Моля изберете тип гориво!");
                return;
            }

            // 2. Валидация на числа (ще хвърли грешка, ако са текст)
            int year = Integer.parseInt(yearField.getText().trim());
            int mileage = Integer.parseInt(mileageField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());
            FuelType fuel = fuelComboBox.getValue();

            // 3. Създаване на обекта
            Car car = new Car();
            car.setBrand(brand);
            car.setModel(model);
            car.setVin(vin);
            car.setYear(year);
            car.setMileage(mileage);
            car.setPrice(price);
            car.setFuel(fuel);

            // Новите коли винаги са "Налични"
            car.setStatus(CarStatus.AVAILABLE);

            // 4. Запис в базата
            carService.addCar(car);

            // 5. Затваряне
            closeWindow();

        } catch (NumberFormatException e) {
            errorLabel.setText("Година, Пробег и Цена трябва да са валидни числа!");
        } catch (Exception e) {
            errorLabel.setText("Грешка при запис: " + e.getMessage());
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