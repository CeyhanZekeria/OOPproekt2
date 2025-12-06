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

    @FXML
    public void onSave() {
        // 1. Валидация
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            errorLabel.setText("Имейлът и паролата са задължителни!");
            return;
        }

        try {
            // 2. Създаване на обект Employee
            Employee employee = new Employee();
            employee.setFirstName(firstNameField.getText());
            employee.setLastName(lastNameField.getText());
            employee.setEmail(emailField.getText());
            employee.setPhone(phoneField.getText());
            employee.setPassword(passwordField.getText());

            // Задаваме роля по подразбиране
            employee.setRole(UserRole.SELLER);

            // 3. Обработка на заплатата
            if (!salaryField.getText().isEmpty()) {
                double salary = Double.parseDouble(salaryField.getText());
                employee.setSalary(salary);
            } else {
                employee.setSalary(0.0);
            }

            // 4. Запис в базата
            employeeService.addEmployee(employee);

            // 5. Затваряне
            closeWindow();

        } catch (NumberFormatException e) {
            errorLabel.setText("Заплатата трябва да е число!");
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
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.close();
    }
}