package bg.autosalon.controllers;

import bg.autosalon.entities.Employee;
import bg.autosalon.services.EmployeeService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class EmployeeListController {


    @FXML private TableView<Employee> employeesTable;


    @FXML private TableColumn<Employee, String> colName;
    @FXML private TableColumn<Employee, String> colEmail;
    @FXML private TableColumn<Employee, String> colPhone;
    @FXML private TableColumn<Employee, String> colRole;
    @FXML private TableColumn<Employee, String> colSalary;


    private final EmployeeService employeeService = new EmployeeService();

    @FXML
    public void initialize() {
        setupColumns();
        loadEmployees();
    }

    private void setupColumns() {

        colName.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getFirstName() + " " + cell.getValue().getLastName()
        ));


        colEmail.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));
        colPhone.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPhone()));


        colRole.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getRole().toString()
        ));


        colSalary.setCellValueFactory(cell -> new SimpleStringProperty(
                String.format("%.2f BGN.", cell.getValue().getSalary())
        ));
    }

    private void loadEmployees() {

        employeesTable.setItems(FXCollections.observableArrayList(employeeService.getAllEmployees()));
    }

    @FXML
    public void onAddEmployee() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bg/autosalon/views/add_employee.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("New employee");
            stage.setScene(new Scene(root));


            stage.initModality(Modality.APPLICATION_MODAL);


            stage.showAndWait();


            loadEmployees();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("ERROR", "This window can not open!");
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}