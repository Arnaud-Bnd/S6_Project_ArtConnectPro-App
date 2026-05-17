package com.project.artconnect.ui;

import com.project.artconnect.model.Artwork;
import com.project.artconnect.model.Artist;
import com.project.artconnect.service.ArtworkService;
import com.project.artconnect.service.ArtistService;
import com.project.artconnect.util.ServiceProvider;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class ArtworkController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Artwork> artworkTable;

    @FXML
    private TableColumn<Artwork, String> titleColumn;
    @FXML
    private TableColumn<Artwork, String> artistColumn;
    @FXML
    private TableColumn<Artwork, String> typeColumn;
    @FXML
    private TableColumn<Artwork, Double> priceColumn;
    @FXML
    private TableColumn<Artwork, String> statusColumn;

    // NOUVELLES COLONNES
    @FXML
    private TableColumn<Artwork, Integer> yearColumn;
    @FXML
    private TableColumn<Artwork, String> mediumColumn;
    @FXML
    private TableColumn<Artwork, String> dimensionsColumn;
    @FXML
    private TableColumn<Artwork, String> descriptionColumn;

    @FXML
    private TextField idField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField creationYearField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField mediumField;
    @FXML
    private TextField dimensionsField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField priceField;
    @FXML
    private ComboBox<Artwork.Status> statusCombo;
    @FXML
    private TextField artistField;

    private final ArtworkService artworkService = ServiceProvider.getArtworkService();
    private final ArtistService artistService = ServiceProvider.getArtistService();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("title"));
        typeColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("type"));
        priceColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("price"));
        yearColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("creationYear"));
        mediumColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("medium"));
        dimensionsColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dimensions"));
        descriptionColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("description"));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus() != null ? cellData.getValue().getStatus().name() : ""));
        artistColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArtist() != null ? cellData.getValue().getArtist().getName() : "Unknown"));
        statusCombo.setItems(FXCollections.observableArrayList(Artwork.Status.values()));

        refreshTable();

        artworkTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selected) -> {
            if (selected != null) {
                idField.setText(String.valueOf(selected.getId()));
                titleField.setText(selected.getTitle());
                creationYearField.setText(selected.getCreationYear() != null ? String.valueOf(selected.getCreationYear()) : "");
                typeField.setText(selected.getType());
                mediumField.setText(selected.getMedium());
                dimensionsField.setText(selected.getDimensions());
                descriptionField.setText(selected.getDescription());
                priceField.setText(String.valueOf(selected.getPrice()));
                statusCombo.setValue(selected.getStatus());
                artistField.setText(selected.getArtist() != null ? selected.getArtist().getName() : "");
            }
        });
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();

        List<Artwork> results = artworkService.getAllArtworks()
                .stream()
                .filter(a -> query == null || query.isBlank()
                        || a.getTitle().toLowerCase().contains(query.toLowerCase()))
                .toList();

        artworkTable.setItems(FXCollections.observableArrayList(results));
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        refreshTable();
    }

    @FXML
    private void handleAdd() {
        try {
            Artwork artwork = new Artwork();

            artwork.setTitle(titleField.getText());
            artwork.setType(typeField.getText());
            artwork.setMedium(mediumField.getText());
            artwork.setDimensions(dimensionsField.getText());
            artwork.setDescription(descriptionField.getText());

            if (!creationYearField.getText().isBlank()) {
                artwork.setCreationYear(Integer.parseInt(creationYearField.getText()));
            }

            double price;
            try {
                price = Double.parseDouble(priceField.getText());
            } catch (NumberFormatException e) {
                showError("Prix invalide.");
                return;
            }
            artwork.setPrice(price);

            artwork.setStatus(statusCombo.getValue());

            Artist artist = artistService.getArtistByName(artistField.getText()).orElse(null);
            artwork.setArtist(artist);

            artworkService.createArtwork(artwork);

            refreshTable();
            clearForm();

        } catch (Exception e) {
            showError("Erreur ajout œuvre : " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        Artwork selected = artworkTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Sélectionnez une œuvre.");
            return;
        }

        try {
            selected.setTitle(titleField.getText());
            selected.setType(typeField.getText());
            selected.setMedium(mediumField.getText());
            selected.setDimensions(dimensionsField.getText());
            selected.setDescription(descriptionField.getText());

            if (!creationYearField.getText().isBlank()) {
                selected.setCreationYear(Integer.parseInt(creationYearField.getText()));
            }

            selected.setPrice(Double.parseDouble(priceField.getText()));
            selected.setStatus(statusCombo.getValue());

            selected.setArtist(
                    artistService.getArtistByName(artistField.getText()).orElse(null)
            );

            artworkService.updateArtwork(selected);

            refreshTable();
            clearForm();

        } catch (Exception e) {
            showError("Erreur update œuvre : " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        Artwork selected = artworkTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Sélectionnez une œuvre.");
            return;
        }

        artworkService.deleteArtwork(selected.getTitle());
        refreshTable();
        clearForm();
    }

    private void refreshTable() {
        artworkTable.setItems(FXCollections.observableArrayList(
                artworkService.getAllArtworks()
        ));
    }

    private void clearForm() {
        idField.clear();
        titleField.clear();
        creationYearField.clear();
        typeField.clear();
        mediumField.clear();
        dimensionsField.clear();
        descriptionField.clear();
        priceField.clear();
        statusCombo.setValue(null);
        artistField.clear();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}