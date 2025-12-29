package bg.autosalon.controllers;

import bg.autosalon.entities.Employee;
import bg.autosalon.enums.UserRole;
import bg.autosalon.services.EmployeeService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddEmployeeController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private TextField salaryField;
    @FXML private Label errorLabel;

    private final EmployeeService employeeService = new EmployeeService();
    private Employee employeeToEdit = null;

    public void setEmployeeToEdit(Employee employee) {
        this.employeeToEdit = employee;
        firstNameField.setText(employee.getFirstName());
        lastNameField.setText(employee.getLastName());
        emailField.setText(employee.getEmail());
        phoneField.setText(employee.getPhone());
        passwordField.setText(employee.getPassword());
        salaryField.setText(String.valueOf(employee.getSalary()));
    }

    @FXML
    public void onSave() {
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            errorLabel.setText("Email and password are required!");
            return;
        }

        try {
            if (employeeToEdit != null) {

                employeeToEdit.setFirstName(firstNameField.getText());
                employeeToEdit.setLastName(lastNameField.getText());
                employeeToEdit.setEmail(emailField.getText());
                employeeToEdit.setPhone(phoneField.getText());
                employeeToEdit.setPassword(passwordField.getText());

                if (!salaryField.getText().isEmpty()) {
                    employeeToEdit.setSalary(Double.parseDouble(salaryField.getText()));
                }

                employeeService.updateEmployee(employeeToEdit);
            } else {

                Employee employee = new Employee();
                employee.setFirstName(firstNameField.getText());
                employee.setLastName(lastNameField.getText());
                employee.setEmail(emailField.getText());
                employee.setPhone(phoneField.getText());
                employee.setPassword(passwordField.getText());
                employee.setRole(UserRole.SELLER);

                if (!salaryField.getText().isEmpty()) {
                    employee.setSalary(Double.parseDouble(salaryField.getText()));
                } else {
                    employee.setSalary(0.0);
                }

                employeeService.addEmployee(employee);
            }
            closeWindow();
        } catch (NumberFormatException e) {
            errorLabel.setText("Salary must be a valid number!");
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
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.close();
    }
}