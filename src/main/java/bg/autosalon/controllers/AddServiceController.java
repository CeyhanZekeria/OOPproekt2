package bg.autosalon.controllers;

import bg.autosalon.entities.Car;
import bg.autosalon.entities.ServiceRecord;
import bg.autosalon.enums.ServiceType;
import bg.autosalon.services.CarService;
import bg.autosalon.services.ServiceRecordService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.time.LocalDate;

public class AddServiceController {

    @FXML private ComboBox<Car> carCombo;
    @FXML private ComboBox<ServiceType> typeCombo;
    @FXML private DatePicker datePicker;
    @FXML private TextField priceField;
    @FXML private TextArea descArea;
    @FXML private Label errorLabel;

    private final CarService carService = new CarService();
    private final ServiceRecordService serviceService = new ServiceRecordService();
    private ServiceRecord recordToEdit = null;

    @FXML
    public void initialize() {
        carCombo.setItems(FXCollections.observableArrayList(carService.getAllCars()));
        typeCombo.getItems().setAll(ServiceType.values());
        datePicker.setValue(LocalDate.now());

        carCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Car c) { return c == null ? "" : c.getBrand() + " " + c.getModel() + " (" + c.getVin() + ")"; }
            @Override public Car fromString(String s) { return null; }
        });
    }

    public void setRecordToEdit(ServiceRecord record) {
        this.recordToEdit = record;
        carCombo.setValue(record.getCar());
        carCombo.setDisable(true);
        typeCombo.setValue(record.getType());
        datePicker.setValue(record.getDate());
        priceField.setText(String.valueOf(record.getPrice()));
        descArea.setText(record.getDescription());
    }

    @FXML
    public void onSave() {
        if (carCombo.getValue() == null || typeCombo.getValue() == null || descArea.getText().isEmpty() || priceField.getText().isEmpty()) {
            errorLabel.setText("All fields are required!");
            return;
        }

        try {
            double price = Double.parseDouble(priceField.getText());

            if (recordToEdit != null) {
                recordToEdit.setType(typeCombo.getValue());
                recordToEdit.setDate(datePicker.getValue());
                recordToEdit.setDescription(descArea.getText());
                recordToEdit.setPrice(price);
                serviceService.updateRecord(recordToEdit);
            } else {
                ServiceRecord record = new ServiceRecord();
                record.setCar(carCombo.getValue());
                record.setType(typeCombo.getValue());
                record.setDate(datePicker.getValue());
                record.setDescription(descArea.getText());
                record.setPrice(price);
                serviceService.addRecord(record);
            }
            closeWindow();
        } catch (NumberFormatException e) {
            errorLabel.setText("Price must be a valid number!");
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML public void onCancel() { closeWindow(); }
    private void closeWindow() { ((Stage) descArea.getScene().getWindow()).close(); }
}