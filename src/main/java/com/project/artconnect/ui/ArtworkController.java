package com.project.artconnect.ui;

import com.project.artconnect.model.Artwork;
import com.project.artconnect.service.ArtworkService;
import com.project.artconnect.service.ArtistService;
import com.project.artconnect.util.ServiceProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ArtworkController {
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Artwork> artworkTable;
    @FXML
    private TableColumn<Artwork, String> titleColumn;
    @FXML
    private TableColumn<Artwork, String> typeColumn;
    @FXML
    private TableColumn<Artwork, Double> priceColumn;
    @FXML
    private TableColumn<Artwork, String> statusColumn;
    @FXML
    private TableColumn<Artwork, String> artistColumn;

    @FXML
    private TextField titleField;
    @FXML
    private TextField typeField;
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
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        artistColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getArtist() != null ? cellData.getValue().getArtist().getName() : "Unknown"));

        statusCombo.setItems(FXCollections.observableArrayList(Artwork.Status.values()));

        try {
            artworkTable.setItems(FXCollections.observableArrayList(artworkService.getAllArtworks()));
        } catch (Exception e) {
            showError("Erreur lors du chargement des œuvres : " + e.getMessage());
        }

        artworkTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selectedArtwork) -> {
            if (selectedArtwork != null) {
                titleField.setText(selectedArtwork.getTitle());
                typeField.setText(selectedArtwork.getType());
                priceField.setText(String.valueOf(selectedArtwork.getPrice()));
                statusCombo.setValue(selectedArtwork.getStatus());
                artistField.setText(selectedArtwork.getArtist() != null ? selectedArtwork.getArtist().getName() : "");
            }
        });
    }
    
    @FXML
    private void handleSearch() {
        String query = searchField.getText();

        List<Artwork> results = artworkService.getAllArtworks()
                .stream()
                .filter(a -> query == null
                        || query.isBlank()
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

            double price;
            try {
                price = Double.parseDouble(priceField.getText());
            } catch (NumberFormatException e) {
                showError("Prix invalide.");
                return;
            }
            artwork.setPrice(price);

            artwork.setStatus(statusCombo.getValue());
            artwork.setArtist(artistService.getArtistByName(artistField.getText()).orElse(null));

            artworkService.createArtwork(artwork);

            refreshTable();
            clearForm();
        } catch (Exception e) {
            showError("Erreur lors de l'ajout de l'œuvre : " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        Artwork selectedArtwork = artworkTable.getSelectionModel().getSelectedItem();

        if (selectedArtwork == null) {
            showAlert("Sélectionnez un œuvre à mettre à jour.");
            return;
        }

        try {
            selectedArtwork.setTitle(titleField.getText());
            selectedArtwork.setType(typeField.getText());

            double price;
            try {
                price = Double.parseDouble(priceField.getText());
            } catch (NumberFormatException e) {
                showError("Prix invalide.");
                return;
            }
            selectedArtwork.setPrice(price);

            selectedArtwork.setStatus(statusCombo.getValue());
            selectedArtwork.setArtist(artistService.getArtistByName(artistField.getText()).orElse(null));

            artworkService.updateArtwork(selectedArtwork);

            refreshTable();
            clearForm();
        } catch (Exception e) {
            showError("Erreur lors de la mise à jour de l'œuvre : " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        Artwork selectedArtwork = artworkTable.getSelectionModel().getSelectedItem();

        if (selectedArtwork == null) {
            showAlert("Sélectionnez un œuvre à supprimer.");
            return;
        }

        try {
            artworkService.deleteArtwork(selectedArtwork.getTitle());
            refreshTable();
            clearForm();
        } catch (Exception e) {
            showError("Erreur lors de la suppression de l'œuvre : " + e.getMessage());
        }
    }

    private void refreshTable() {
        artworkTable.setItems(FXCollections.observableArrayList(artworkService.getAllArtworks()));
    }

    private void clearForm() {
        titleField.clear();
        typeField.clear();
        priceField.clear();
        statusCombo.setValue(null);
        artistField.clear();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("ERROR —");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning —");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
