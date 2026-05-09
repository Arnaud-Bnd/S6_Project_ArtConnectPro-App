package com.project.artconnect.ui;

import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.service.CommunityService;
import com.project.artconnect.util.ServiceProvider;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class CommunityController {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<CommunityMember> memberTable;
    @FXML
    private TableColumn<CommunityMember, String> nameColumn;
    @FXML
    private TableColumn<CommunityMember, String> emailColumn;
    @FXML
    private TableColumn<CommunityMember, String> cityColumn;

    private final CommunityService communityService = ServiceProvider.getCommunityService();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));

        refreshTable();
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim().toLowerCase();
        List<CommunityMember> results = communityService.getAllMembers()
                        .stream()
                        .filter(m -> query.isBlank()
                                || m.getName().toLowerCase().contains(query)
                                || m.getCity().toLowerCase().contains(query)
                                || m.getEmail().toLowerCase().contains(query))
                        .toList();

        memberTable.setItems(FXCollections.observableArrayList(results));
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        refreshTable();
    }

    private void refreshTable() {

        memberTable.setItems(FXCollections.observableArrayList(communityService.getAllMembers()));
    }
}