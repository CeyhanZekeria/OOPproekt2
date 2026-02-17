package bg.autosalon.controllers;

import bg.autosalon.entities.Employee;
import bg.autosalon.services.EmployeeService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class EmployeeListController {

    @FXML
    private TableView<Employee> employeesTable;
    @FXML
    private TableColumn<Employee, String> colFirstName;
    @FXML
    private TableColumn<Employee, String> colLastName;
    @FXML
    private TableColumn<Employee, String> colEmail;
    @FXML
    private TableColumn<Employee, String> colPhone;
    @FXML
    private TableColumn<Employee, String> colSalary;

    private final EmployeeService employeeService = new EmployeeService();

    @FXML
    public void initialize() {
        colFirstName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFirstName()));
        colLastName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLastName()));
        colEmail.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));
        colPhone.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPhone()));
        colSalary.setCellValueFactory(cell -> new SimpleStringProperty(String.format("%.2f", cell.getValue().getSalary())));

        loadEmployees();
    }

    private void loadEmployees() {
        employeesTable.setItems(FXCollections.observableArrayList(employeeService.getAllEmployees()));
    }

    @FXML
    public void onAddEmployee() {
        openEmployeeForm(null);
    }

    @FXML
    public void onEditEmployee() {
        Employee selected = employeesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Please select an employee to edit!");
            return;
        }
        openEmployeeForm(selected);
    }

    @FXML
    public void onDeleteEmployee() {
        Employee selected = employeesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Please select an employee to delete!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setContentText("Delete employee: " + selected.getFirstName() + " " + selected.getLastName() + "?");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            try {
                employeeService.deleteEmployee(selected.getId());
                loadEmployees();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, e.getMessage());
            }
        }
    }

    private void openEmployeeForm(Employee employeeToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bg/autosalon/views/add_employee.fxml"));
            Parent root = loader.load();

            if (employeeToEdit != null) {
                AddEmployeeController controller = loader.getController();
                controller.setEmployeeToEdit(employeeToEdit);
            }

            Stage stage = new Stage();
            stage.setTitle(employeeToEdit == null ? "New Employee" : "Edit Employee");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadEmployees();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}