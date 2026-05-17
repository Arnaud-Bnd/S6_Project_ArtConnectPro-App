package com.project.artconnect.ui;

import com.project.artconnect.dao.ExhibitionDao;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.service.GalleryService;
import com.project.artconnect.util.ServiceProvider;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class GalleryController {

    @FXML private TextField searchField;

    @FXML private ListView<Gallery> galleryList;
    @FXML private ListView<String> exhibitionList;

    @FXML private Label galleryDetailsLabel;

    // NOUVEAUX CHAMPS DÉTAILS
    @FXML private Label nameLabel;
    @FXML private Label addressLabel;
    @FXML private Label ownerLabel;
    @FXML private Label openingHoursLabel;
    @FXML private Label phoneLabel;
    @FXML private Label ratingLabel;
    @FXML private Label websiteLabel;

    private final GalleryService galleryService = ServiceProvider.getGalleryService();
    private final ExhibitionDao exhibitionDao = ServiceProvider.getExhibitionDao();

    @FXML
    public void initialize() {

        refreshGalleryList();

        galleryList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Gallery item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(
                            item.getName()
                                    + " - " + item.getAddress()
                                    + " (" + item.getRating() + "/5)"
                    );
                }
            }
        });

        galleryList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {

            if (selected != null) {

                // LABEL DÉTAILLÉS
                nameLabel.setText(selected.getName());
                addressLabel.setText(selected.getAddress());
                ownerLabel.setText(selected.getOwnerName());
                openingHoursLabel.setText(selected.getOpeningHours());
                phoneLabel.setText(selected.getContactPhone());
                ratingLabel.setText(String.valueOf(selected.getRating()));
                websiteLabel.setText(selected.getWebsite());

                // EXHIBITIONS liées
                List<String> exhibitions = exhibitionDao.findAll()
                        .stream()
                        .filter(e -> e.getGallery() != null
                                && e.getGallery().getId().equals(selected.getId()))
                        .map(Exhibition::getTitle)
                        .toList();

                exhibitionList.setItems(
                        FXCollections.observableArrayList(exhibitions)
                );
            }
        });
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim().toLowerCase();

        List<Gallery> results = galleryService.getAllGalleries()
                .stream()
                .filter(g ->
                        query.isBlank()
                                || g.getName().toLowerCase().contains(query)
                                || g.getAddress().toLowerCase().contains(query)
                                || (g.getOwnerName() != null && g.getOwnerName().toLowerCase().contains(query))
                )
                .toList();

        galleryList.setItems(FXCollections.observableArrayList(results));
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        refreshGalleryList();

        exhibitionList.getItems().clear();

        nameLabel.setText("");
        addressLabel.setText("");
        ownerLabel.setText("");
        openingHoursLabel.setText("");
        phoneLabel.setText("");
        ratingLabel.setText("");
        websiteLabel.setText("");
    }

    private void refreshGalleryList() {
        galleryList.setItems(FXCollections.observableArrayList(galleryService.getAllGalleries()));
    }
}