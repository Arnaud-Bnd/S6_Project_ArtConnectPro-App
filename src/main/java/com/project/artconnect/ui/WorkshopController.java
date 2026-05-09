package com.project.artconnect.ui;

import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.service.CommunityService;
import com.project.artconnect.service.WorkshopService;
import com.project.artconnect.util.ServiceProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.util.List;

public class WorkshopController {
    @FXML
    private TextField searchField;
    @FXML
    private TextField memberField;
    @FXML
    private TableView<Workshop> workshopTable;
    @FXML
    private TableColumn<Workshop, String> titleColumn;
    @FXML
    private TableColumn<Workshop, LocalDateTime> dateColumn;
    @FXML
    private TableColumn<Workshop, String> instructorColumn;
    @FXML
    private TableColumn<Workshop, Double> priceColumn;
    @FXML
    private TableColumn<Workshop, String> levelColumn;

    @FXML
    private Label workshopDetailsLabel;

    private final WorkshopService workshopService = ServiceProvider.getWorkshopService();

    private final CommunityService communityService = ServiceProvider.getCommunityService();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        instructorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInstructor() != null ? cellData.getValue().getInstructor().getName() : "Unknown"));

        refreshTable();

        // Sélection workshop
        workshopTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selectedWorkshop) -> {
                    if (selectedWorkshop != null) {
                        workshopDetailsLabel.setText("Workshop: " + selectedWorkshop.getTitle() + "\nDate: " + selectedWorkshop.getDate() + "\nPrice: " + selectedWorkshop.getPrice() + "\nLevel: " + selectedWorkshop.getLevel());
                    }
                });
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim().toLowerCase();

        List<Workshop> results =
                workshopService.getAllWorkshops()
                        .stream()
                        .filter(w -> query.isBlank()
                                || w.getTitle().toLowerCase().contains(query))
                        .toList();

        workshopTable.setItems(FXCollections.observableArrayList(results));
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        refreshTable();
        workshopDetailsLabel.setText("");
    }

    @FXML
    private void handleBooking() {
        Workshop selectedWorkshop = workshopTable.getSelectionModel().getSelectedItem();

        if (selectedWorkshop == null) {
            showAlert("Sélectionnez un workshop.");
            return;
        }

        String memberName = memberField.getText();

        if (memberName == null || memberName.isBlank()) {
            showAlert("Entrez un nom de membre.");
            return;
        }

        CommunityMember member = communityService.getMemberByName(memberName).orElse(null);

        if (member == null) {
            showError("Membre introuvable.");
            return;
        }

        workshopService.bookWorkshop(selectedWorkshop, member);
        showInfo("Réservation effectuée.");
    }

    private void refreshTable() {
        workshopTable.setItems(FXCollections.observableArrayList(workshopService.getAllWorkshops()));
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("ERROR");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("WARNING");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("INFO");
        alert.setContentText(message);
        alert.showAndWait();
    }
}