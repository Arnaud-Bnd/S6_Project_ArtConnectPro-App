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

    @FXML private TextField searchField;
    @FXML private TextField memberField;

    @FXML private TableView<Workshop> workshopTable;

    @FXML private TableColumn<Workshop, String> titleColumn;
    @FXML private TableColumn<Workshop, LocalDateTime> dateColumn;
    @FXML private TableColumn<Workshop, String> instructorColumn;
    @FXML private TableColumn<Workshop, Double> priceColumn;
    @FXML private TableColumn<Workshop, String> levelColumn;

    // NOUVELLES COLONNES
    @FXML private TableColumn<Workshop, Integer> durationColumn;
    @FXML private TableColumn<Workshop, Integer> maxParticipantsColumn;
    @FXML private TableColumn<Workshop, String> locationColumn;
    @FXML private TableColumn<Workshop, String> descriptionColumn;

    @FXML private Label workshopDetailsLabel;

    @FXML private Label instructorDetailsLabel;
    @FXML private Label durationLabel;
    @FXML private Label maxParticipantsLabel;
    @FXML private Label locationLabel;
    @FXML private Label descriptionLabel;

    private final WorkshopService workshopService = ServiceProvider.getWorkshopService();
    private final CommunityService communityService = ServiceProvider.getCommunityService();

    @FXML
    public void initialize() {

        // TABLE MAPPING
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));

        durationColumn.setCellValueFactory(new PropertyValueFactory<>("durationMinutes"));
        maxParticipantsColumn.setCellValueFactory(new PropertyValueFactory<>("maxParticipants"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        instructorColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getInstructor() != null
                                ? cellData.getValue().getInstructor().getName()
                                : "Unknown"
                )
        );

        refreshTable();

        workshopTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {

            if (selected != null) {

                workshopDetailsLabel.setText(
                        "Workshop: " + selected.getTitle()
                                + "\nDate: " + selected.getDate()
                                + "\nPrice: " + selected.getPrice()
                                + "\nLevel: " + selected.getLevel()
                );

                instructorDetailsLabel.setText(
                        selected.getInstructor() != null
                                ? selected.getInstructor().getName()
                                : "Unknown"
                );

                durationLabel.setText(selected.getDurationMinutes() + " min");
                maxParticipantsLabel.setText(String.valueOf(selected.getMaxParticipants()));
                locationLabel.setText(selected.getLocation());
                descriptionLabel.setText(selected.getDescription());
            }
        });
    }

    @FXML
    private void handleSearch() {

        String query = searchField.getText().trim().toLowerCase();

        List<Workshop> results = workshopService.getAllWorkshops()
                .stream()
                .filter(w ->
                        query.isBlank()
                                || w.getTitle().toLowerCase().contains(query)
                                || (w.getLevel() != null && w.getLevel().toLowerCase().contains(query))
                                || (w.getLocation() != null && w.getLocation().toLowerCase().contains(query))
                )
                .toList();

        workshopTable.setItems(FXCollections.observableArrayList(results));
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        refreshTable();

        workshopDetailsLabel.setText("");
        instructorDetailsLabel.setText("");
        durationLabel.setText("");
        maxParticipantsLabel.setText("");
        locationLabel.setText("");
        descriptionLabel.setText("");
    }

    @FXML
    private void handleBooking() {

        Workshop selected = workshopTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
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

        workshopService.bookWorkshop(selected, member);

        showInfo("Réservation effectuée.");
    }

    private void refreshTable() {
        workshopTable.setItems(FXCollections.observableArrayList(workshopService.getAllWorkshops()));
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(msg);
        a.showAndWait();
    }
}