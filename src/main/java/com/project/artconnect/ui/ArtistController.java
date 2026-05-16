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
    private TableView<Artist> artistTable;
    @FXML
    private ComboBox<Discipline> disciplineFilter;

    @FXML
    private TableColumn<Artist, String> nameColumn;
    @FXML
    private TableColumn<Artist, String> cityColumn;
    @FXML
    private TableColumn<Artist, String> emailColumn;
    @FXML
    private TableColumn<Artist, Integer> yearColumn;
    @FXML
    private TableColumn<Artist, String> disciplinesColumn;
    @FXML
    private TableColumn<Artist, String> phoneColumn;
    @FXML
    private TableColumn<Artist, String> websiteColumn;
    @FXML
    private TableColumn<Artist, Boolean> activeColumn;

    @FXML
    private TextField nameField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField birthYearField;
    @FXML
    private TextArea bioField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField websiteField;
    @FXML
    private TextField socialMediaField;
    @FXML
    private CheckBox activeCheckBox;

    @FXML
    private ComboBox<Discipline> disciplineCombo;

    @FXML
    private TextField artworksField;

    private final ArtistService artistService = ServiceProvider.getArtistService();

    @FXML
    public void initialize() {

        // TABLE COLUMNS
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("contactEmail"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("birthYear"));
        disciplinesColumn.setCellValueFactory(cellData -> {
            Artist artist = cellData.getValue();

            String disciplines = artist.getDisciplines()
                    .stream()
                    .map(Discipline::getName)
                    .collect(java.util.stream.Collectors.joining(", "));

            return new javafx.beans.property.SimpleStringProperty(disciplines);
        });
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        websiteColumn.setCellValueFactory(new PropertyValueFactory<>("website"));
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("active"));

        // COMBOBOX DISCIPLINES
        disciplineCombo.setItems(FXCollections.observableArrayList(ServiceProvider.getDisciplineService().getAllDisciplines()));

        disciplineCombo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Discipline item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        disciplineCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Discipline item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        disciplineCombo.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Discipline d) {
                return d == null ? "" : d.getName();
            }

            @Override
            public Discipline fromString(String string) {
                return null;
            }
        });

        disciplineFilter.setItems(
                FXCollections.observableArrayList(
                        ServiceProvider.getDisciplineService().getAllDisciplines()
                )
        );

        disciplineFilter.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Discipline item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        disciplineFilter.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Discipline item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        disciplineFilter.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Discipline d) {
                return d == null ? "" : d.getName();
            }

            @Override
            public Discipline fromString(String s) {
                return null;
            }
        });

        // TABLE DATA
        refreshTable();

        // SELECTION LISTENER
        artistTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSelection, selectedArtist) -> {

                    if (selectedArtist == null) return;

                    nameField.setText(selectedArtist.getName());
                    cityField.setText(selectedArtist.getCity());
                    emailField.setText(selectedArtist.getContactEmail());

                    birthYearField.setText(
                            selectedArtist.getBirthYear() != null
                                    ? String.valueOf(selectedArtist.getBirthYear())
                                    : ""
                    );

                    bioField.setText(selectedArtist.getBio());
                    phoneField.setText(selectedArtist.getPhone());
                    websiteField.setText(selectedArtist.getWebsite());
                    socialMediaField.setText(selectedArtist.getSocialMedia());

                    activeCheckBox.setSelected(selectedArtist.isActive());

                    disciplineCombo.setValue(
                            selectedArtist.getDisciplines().isEmpty()
                                    ? null
                                    : selectedArtist.getDisciplines().get(0)
                    );

                    artworksField.setText(
                            selectedArtist.getArtworks()
                                    .stream()
                                    .map(a -> a.getTitle())
                                    .reduce((a, b) -> a + ", " + b)
                                    .orElse("")
                    );
                });
        artistService.getAllArtists().forEach(a -> {
            System.out.println("ARTIST = " + a.getName());
            System.out.println("DISCIPLINES SIZE = " + a.getDisciplines().size());
            a.getDisciplines().forEach(d ->
                    System.out.println(" - " + d.getName())
            );
        });
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        Discipline selectedDiscipline = disciplineFilter.getValue();

        String disciplineName = (selectedDiscipline != null)
                ? selectedDiscipline.getName()
                : null;

        artistTable.setItems(
                FXCollections.observableArrayList(
                        artistService.searchArtists(query, disciplineName, null)
                )
        );
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
            artist.setBio(bioField.getText());
            artist.setPhone(phoneField.getText());
            artist.setWebsite(websiteField.getText());
            artist.setSocialMedia(socialMediaField.getText());
            artist.setActive(activeCheckBox.isSelected());

            Discipline d = disciplineCombo.getValue();
            if (d != null) {
                artist.getDisciplines().add(d);
            }

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
            selectedArtist.setBio(bioField.getText());
            selectedArtist.setPhone(phoneField.getText());
            selectedArtist.setWebsite(websiteField.getText());
            selectedArtist.setSocialMedia(socialMediaField.getText());
            selectedArtist.setActive(activeCheckBox.isSelected());

            selectedArtist.getDisciplines().clear();
            Discipline d = disciplineCombo.getValue();
            if (d != null) {
                selectedArtist.getDisciplines().add(d);
            }

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
            artistService.deleteArtist(String.valueOf(selectedArtist.getId()));

            refreshTable();
            clearForm();

        } catch (Exception e) {
            showError("Erreur lors de la suppression de l'artiste: " + e.getMessage());
        }
    }

    private void refreshTable() {
        artistTable.setItems(
                FXCollections.observableArrayList(
                        artistService.getAllArtists()
                )
        );
    }

    private void clearForm() {

        nameField.clear();
        cityField.clear();
        emailField.clear();
        birthYearField.clear();
        bioField.clear();
        phoneField.clear();
        websiteField.clear();
        socialMediaField.clear();

        disciplineCombo.setValue(null);

        artworksField.clear();
        activeCheckBox.setSelected(false);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("ERROR");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}