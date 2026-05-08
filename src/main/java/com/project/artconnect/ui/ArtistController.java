package com.project.artconnect.ui;

import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.service.ArtistService;
import com.project.artconnect.util.ServiceProvider;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ArtistController {
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<Discipline> disciplineFilter;
    @FXML
    private TableView<Artist> artistTable;
    @FXML
    private TableColumn<Artist, String> nameColumn;
    @FXML
    private TableColumn<Artist, String> cityColumn;
    @FXML
    private TableColumn<Artist, String> emailColumn;
    @FXML
    private TableColumn<Artist, Integer> yearColumn;

    @FXML
    private TextField nameField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField birthYearField;

    private final ArtistService artistService = ServiceProvider.getArtistService();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("contactEmail"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("birthYear"));

        try {
            disciplineFilter.setItems(FXCollections.observableArrayList(artistService.getAllDisciplines()));
            refreshTable();
        } catch (Exception e) {
            showError("Erreur lors du chargement des artistes : " + e.getMessage());
        }

        artistTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selectedArtist) -> {
            if (selectedArtist != null) {
                nameField.setText(selectedArtist.getName());
                cityField.setText(selectedArtist.getCity());
                emailField.setText(selectedArtist.getContactEmail());
                birthYearField.setText(String.valueOf(selectedArtist.getBirthYear()));
            }
        });
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        Discipline d = disciplineFilter.getValue();
        String dName = (d != null) ? d.getName() : null;
        artistTable.setItems(FXCollections.observableArrayList(artistService.searchArtists(query, dName, null)));
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        disciplineFilter.setValue(null);
        refreshTable();
    }

    @FXML
    private void handleAdd() {
        try {
            Artist artist = new Artist();

            artist.setName(nameField.getText());
            artist.setCity(cityField.getText());
            artist.setContactEmail(emailField.getText());
            artist.setBirthYear(Integer.parseInt(birthYearField.getText()));

            artistService.createArtist(artist);

            refreshTable();
            clearForm();
        } catch (Exception e) {
            showError("Erreur lors de l'ajout de l'artiste: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        Artist selectedArtist = artistTable.getSelectionModel().getSelectedItem();

        if (selectedArtist == null) {
            showAlert("Sélectionnez un artiste à mettre à jour.");
            return;
        }

        try {
            selectedArtist.setName(nameField.getText());
            selectedArtist.setCity(cityField.getText());
            selectedArtist.setContactEmail(emailField.getText());
            selectedArtist.setBirthYear(Integer.parseInt(birthYearField.getText()));

            artistService.updateArtist(selectedArtist);

            refreshTable();
            clearForm();
        } catch (Exception e) {
            showError("Erreur lors de la mise à jour de l'artiste: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        Artist selectedArtist = artistTable.getSelectionModel().getSelectedItem();

        if (selectedArtist == null) {
            showAlert("Sélectionnez un artiste à supprimer.");
            return;
        }

        try {
            artistService.deleteArtist(selectedArtist.getName());
            refreshTable();
            clearForm();
        } catch (Exception e) {
            showError("Erreur lors de la suppression de l'artiste: " + e.getMessage());
        }
    }

    private void refreshTable() {
        artistTable.setItems(FXCollections.observableArrayList(artistService.getAllArtists()));
    }

    private void clearForm() {
        nameField.clear();
        cityField.clear();
        emailField.clear();
        birthYearField.clear();
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
