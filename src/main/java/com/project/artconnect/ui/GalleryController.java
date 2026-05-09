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

    @FXML
    private TextField searchField;
    @FXML
    private ListView<Gallery> galleryList;
    @FXML
    private ListView<String> exhibitionList;
    @FXML
    private Label galleryDetailsLabel;

    private final GalleryService galleryService = ServiceProvider.getGalleryService();

    private final ExhibitionDao exhibitionDao = ServiceProvider.getExhibitionDao();

    @FXML
    public void initialize() {

        refreshGalleryList();

        // Affichage custom
        galleryList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Gallery item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " - " + item.getAddress() + " (" + item.getRating() + "/5.0)");
                }
            }
        });

        // Sélection galerie
        galleryList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selectedGallery) -> {
                    if (selectedGallery != null) {
                        galleryDetailsLabel.setText("Gallery: " + selectedGallery.getName() + "\nAddress: " + selectedGallery.getAddress() + "\nRating: " + selectedGallery.getRating());

                        List<String> exhibitions = exhibitionDao.findAll()
                                .stream()
                                .filter(e -> e.getGallery() != null
                                        && e.getGallery().getId().equals(selectedGallery.getId()))
                                .map(Exhibition::getTitle)
                                .toList();

                        exhibitionList.setItems(FXCollections.observableArrayList(exhibitions));
                    }
                });
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim().toLowerCase();

        List<Gallery> results =
                galleryService.getAllGalleries()
                        .stream()
                        .filter(g -> query.isBlank()
                                || g.getName().toLowerCase().contains(query)
                                || g.getAddress().toLowerCase().contains(query))
                        .toList();

        galleryList.setItems(FXCollections.observableArrayList(results));
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        refreshGalleryList();
        exhibitionList.getItems().clear();
        galleryDetailsLabel.setText("");
    }

    private void refreshGalleryList() {
        galleryList.setItems(FXCollections.observableArrayList(galleryService.getAllGalleries()));
    }
}