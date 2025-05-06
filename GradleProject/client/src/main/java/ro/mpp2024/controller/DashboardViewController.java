package ro.mpp2024.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ro.mpp2024.models.Participant;
import ro.mpp2024.models.Result;
import ro.mpp2024.models.User;
import ro.mpp2024.service.ITriathlonService;
import ro.mpp2024.service.IUserObserver;
import ro.mpp2024.models.ParticipantEventPoints;

import java.util.List;

public class DashboardViewController implements IUserObserver {

    @FXML private Label arbiterNameLabel;
    @FXML private Label eventLabel;
    @FXML private Label selectedParticipantLabel;
    @FXML private Label reportEventLabel;
    @FXML private TableView<Participant> participantTable;
    @FXML private TableColumn<Participant, String> nameColumn;
    @FXML private TableColumn<Participant, Integer> pointsColumn;
    @FXML private TableView<ParticipantEventPoints> reportTable;
    @FXML private TableColumn<ParticipantEventPoints, String> reportNameColumn;
    @FXML private TableColumn<ParticipantEventPoints, Integer> reportPointsColumn;
    @FXML private TextField pointsInputField;
    @FXML private Button addPointsButton;
    @FXML private Button reportButton;
    @FXML private Button logoutButton;
    @FXML private TextField newParticipantNameField;

    private ITriathlonService service;
    private User loggedInUser;
    private final ObservableList<Participant> participantsModel = FXCollections.observableArrayList();
    private final ObservableList<ParticipantEventPoints> reportModel = FXCollections.observableArrayList();

    public void setService(ITriathlonService service) {
        this.service = service;
    }

    public void setUser(User user) {
        this.loggedInUser = user;
        if (user != null) {
            arbiterNameLabel.setText("Logged in as: " + user.getName());
            eventLabel.setText(user.getEvent().name());
            reportEventLabel.setText(user.getEvent().name());
            
            // Register this controller as an observer
            service.registerObserver(this, user.getName());
            
            reloadParticipantList();
            loadReportData();
        }
    }

    public void initialize() {
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        pointsColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getTotalPoints()).asObject());
        participantTable.setItems(participantsModel);

        reportNameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getParticipantName()));
        reportPointsColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getPoints()).asObject());
        reportTable.setItems(reportModel);

        participantTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedParticipantLabel.setText(newSelection.getName() + " (Current points: " + newSelection.getTotalPoints() + ")");
            } else {
                selectedParticipantLabel.setText("No participant selected");
            }
        });

        selectedParticipantLabel.setText("No participant selected");
    }

    public void initData() {
        if (loggedInUser != null) {
            arbiterNameLabel.setText("Logged in as: " + loggedInUser.getName());
            eventLabel.setText(loggedInUser.getEvent().name());
            reportEventLabel.setText(loggedInUser.getEvent().name());
            reloadParticipantList();
            loadReportData();
        }
    }

    private void reloadParticipantList() {
        try {
            List<Participant> participants = service.getAllParticipantsWithTotalPoints();
            participantsModel.setAll(participants);
        } catch (Exception e) {
            showError("Error loading participants: " + e.getMessage());
        }
    }

    private void loadReportData() {
        try {
            List<ParticipantEventPoints> results = service.getAggregatedResultsByEvent(loggedInUser.getEvent().name());
            reportModel.setAll(results);
        } catch (Exception e) {
            showError("Error loading report data: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddPoints() {
        Participant selected = participantTable.getSelectionModel().getSelectedItem();
        String pointsText = pointsInputField.getText();

        if (selected == null || pointsText.isEmpty()) {
            showError("Please select a participant and enter points.");
            return;
        }

        try {
            int points = Integer.parseInt(pointsText);
            if (points < 0) {
                showError("Points must be a positive number.");
                return;
            }

            Result result = new Result(selected, loggedInUser.getEvent(), points);
            service.addResult(result);
            pointsInputField.clear();
            showInformation("Added " + points + " points to " + selected.getName());
        } catch (NumberFormatException e) {
            showError("Points must be a valid number.");
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }


    @FXML
    private void handleRefreshReport() {
        loadReportData();
    }

    @FXML
    private void handleLogout() {
        try {
            // Unregister the observer before logout
            if (loggedInUser != null) {
                service.removeObserver(loggedInUser.getName());
            }
            service.logout();
        } catch (Exception e) {
            showError("Logout error: " + e.getMessage());
        }
        participantTable.getScene().getWindow().hide();
    }

    @FXML
    private void handleAddParticipant() {
        String name = newParticipantNameField.getText().trim();
        if (name.isEmpty()) {
            showError("Participant name cannot be empty.");
            return;
        }

        try {
            service.addParticipant(name);
            newParticipantNameField.clear();
            reloadParticipantList();
            showInformation("Participant '" + name + "' added.");
        } catch (Exception e) {
            showError("Error adding participant: " + e.getMessage());
        }
    }

    // Implement the IUserObserver interface method
    @Override
    public void notifyDataChanged() {
        Platform.runLater(() -> {
            reloadParticipantList();
            loadReportData();
        });
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showInformation(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
