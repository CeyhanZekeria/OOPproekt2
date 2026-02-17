package bg.autosalon.controllers;

import bg.autosalon.config.HibernateUtil;
import bg.autosalon.entities.Car;
import bg.autosalon.entities.Sale;
import bg.autosalon.entities.ServiceRecord;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CarHistoryController {

    @FXML
    private Label carTitleLabel;


    @FXML
    private TableView<Sale> salesTable;
    @FXML
    private TableColumn<Sale, String> colSaleDate;
    @FXML
    private TableColumn<Sale, String> colClient;
    @FXML
    private TableColumn<Sale, String> colPrice;


    @FXML
    private TableView<ServiceRecord> serviceTable;
    @FXML
    private TableColumn<ServiceRecord, String> colServiceDate;
    @FXML
    private TableColumn<ServiceRecord, String> colDescription;
    @FXML
    private TableColumn<ServiceRecord, String> colServicePrice;

    private Car selectedCar;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void setCar(Car car) {
        this.selectedCar = car;
        carTitleLabel.setText("History for: " + car.getBrand() + " " + car.getModel() + " (" + car.getVin() + ")");
        loadData();
    }

    private void loadData() {
        if (selectedCar == null) return;

        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        try {

            TypedQuery<Sale> saleQuery = em.createQuery(
                    "SELECT s FROM Sale s WHERE s.car.id = :carId ORDER BY s.saleDate DESC", Sale.class);
            saleQuery.setParameter("carId", selectedCar.getId());
            List<Sale> sales = saleQuery.getResultList();


            TypedQuery<ServiceRecord> serviceQuery = em.createQuery(
                    "SELECT sr FROM ServiceRecord sr WHERE sr.car.id = :carId ORDER BY sr.date DESC", ServiceRecord.class);
            serviceQuery.setParameter("carId", selectedCar.getId());
            List<ServiceRecord> services = serviceQuery.getResultList();


            setupTables(sales, services);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private void setupTables(List<Sale> sales, List<ServiceRecord> services) {

        colSaleDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSaleDate().format(formatter)));
        colClient.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getClient().getFirstName() + " " + cell.getValue().getClient().getLastName()));
        colPrice.setCellValueFactory(cell -> new SimpleStringProperty(String.format("%.2f BGN", cell.getValue().getFinalPrice())));

        salesTable.setItems(FXCollections.observableArrayList(sales));


        colServiceDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDate().format(formatter)));
        colDescription.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDescription()));
        colServicePrice.setCellValueFactory(cell -> new SimpleStringProperty(String.format("%.2f BGN", cell.getValue().getPrice())));

        serviceTable.setItems(FXCollections.observableArrayList(services));
    }
}