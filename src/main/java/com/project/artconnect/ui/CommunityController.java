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
    private TableColumn<CommunityMember, Integer> idColumn;
    @FXML
    private TableColumn<CommunityMember, String> nameColumn;
    @FXML
    private TableColumn<CommunityMember, String> emailColumn;
    @FXML
    private TableColumn<CommunityMember, Integer> birthYearColumn;
    @FXML
    private TableColumn<CommunityMember, String> phoneColumn;
    @FXML
    private TableColumn<CommunityMember, String> cityColumn;
    @FXML
    private TableColumn<CommunityMember, String> membershipTypeColumn;

    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField birthYearField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField membershipTypeField;

    private final CommunityService communityService = ServiceProvider.getCommunityService();

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        birthYearColumn.setCellValueFactory(new PropertyValueFactory<>("birthYear"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        membershipTypeColumn.setCellValueFactory(new PropertyValueFactory<>("membershipType"));

        refreshTable();

        memberTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selected) -> {

            if (selected != null) {

                idField.setText(String.valueOf(selected.getId()));
                nameField.setText(selected.getName());
                emailField.setText(selected.getEmail());

                birthYearField.setText(
                        selected.getBirthYear() != null
                                ? String.valueOf(selected.getBirthYear())
                                : ""
                );

                phoneField.setText(selected.getPhone());
                cityField.setText(selected.getCity());
                membershipTypeField.setText(selected.getMembershipType());
            }
        });
    }

    @FXML
    private void handleSearch() {

        String query = searchField.getText().trim().toLowerCase();

        List<CommunityMember> results = communityService.getAllMembers()
                .stream()
                .filter(m ->
                        query.isBlank()
                                || m.getName().toLowerCase().contains(query)
                                || m.getEmail().toLowerCase().contains(query)
                                || m.getCity().toLowerCase().contains(query)
                                || m.getMembershipType().toLowerCase().contains(query)
                )
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