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
    @FXML private TextArea descArea;

    private final CarService carService = new CarService();
    private final ServiceRecordService serviceService = new ServiceRecordService();

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

    @FXML
    public void onSave() {
        if (carCombo.getValue() == null || typeCombo.getValue() == null) {
            return;
        }

        ServiceRecord record = new ServiceRecord();
        record.setCar(carCombo.getValue());
        record.setType(typeCombo.getValue());
        record.setDate(datePicker.getValue());
        record.setDescription(descArea.getText());

        serviceService.addRecord(record);

        closeWindow();
    }

    @FXML public void onCancel() { closeWindow(); }
    private void closeWindow() { ((Stage) descArea.getScene().getWindow()).close(); }
}