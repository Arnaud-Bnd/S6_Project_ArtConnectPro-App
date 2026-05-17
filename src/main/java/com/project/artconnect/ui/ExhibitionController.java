package com.project.artconnect.ui;

import com.project.artconnect.dao.ExhibitionDao;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.util.ServiceProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class ExhibitionController {

    @FXML
    private TableView<Exhibition> exhibitionTable;

    @FXML
    private TableColumn<Exhibition, Integer> idColumn;
    @FXML
    private TableColumn<Exhibition, String> titleColumn;
    @FXML
    private TableColumn<Exhibition, LocalDate> startDateColumn;
    @FXML
    private TableColumn<Exhibition, LocalDate> endDateColumn;
    @FXML
    private TableColumn<Exhibition, String> descriptionColumn;
    @FXML
    private TableColumn<Exhibition, String> galleryColumn;
    @FXML
    private TableColumn<Exhibition, String> curatorColumn;
    @FXML
    private TableColumn<Exhibition, String> themeColumn;

    @FXML
    private TextField idField;
    @FXML
    private TextField titleField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField galleryField;
    @FXML
    private TextField curatorField;
    @FXML
    private TextField themeField;

    private final ExhibitionDao exhibitionDao = ServiceProvider.getExhibitionDao();

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        themeColumn.setCellValueFactory(new PropertyValueFactory<>("theme"));
        curatorColumn.setCellValueFactory(new PropertyValueFactory<>("curatorName"));

        galleryColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getGallery() != null
                                ? cellData.getValue().getGallery().getName()
                                : "Unknown"
                )
        );

        refreshData();

        exhibitionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selected) -> {

            if (selected != null) {

                idField.setText(String.valueOf(selected.getId()));
                titleField.setText(selected.getTitle());

                startDatePicker.setValue(selected.getStartDate());
                endDatePicker.setValue(selected.getEndDate());

                descriptionField.setText(selected.getDescription());

                galleryField.setText(
                        selected.getGallery() != null
                                ? selected.getGallery().getName()
                                : ""
                );

                curatorField.setText(selected.getCuratorName());
                themeField.setText(selected.getTheme());
            }
        });
    }

    private void refreshData() {
        List<Exhibition> all = exhibitionDao.findAll();
        exhibitionTable.setItems(FXCollections.observableArrayList(all));
    }
}