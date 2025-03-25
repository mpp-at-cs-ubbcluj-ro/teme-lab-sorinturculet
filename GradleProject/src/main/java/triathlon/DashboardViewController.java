package triathlon;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Participant;
import models.Result;
import models.User;
import service.TriathlonService;
import service.TriathlonService.ParticipantEventPoints;

import java.util.List;

public class DashboardViewController implements TriathlonService.DataChangeListener {

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

    private TriathlonService service;
    private User loggedInUser;
    private ObservableList<Participant> participantsModel = FXCollections.observableArrayList();
    private ObservableList<ParticipantEventPoints> reportModel = FXCollections.observableArrayList();

    public void setService(TriathlonService service) {
        this.service = service;
        // Register as a listener for data changes
        service.addDataChangeListener(this);
    }

    public void setUser(User user) {
        this.loggedInUser = user;
        
        // Do user-dependent initialization
        if (user != null) {
            arbiterNameLabel.setText("Logged in as: " + user.getName());
            eventLabel.setText(user.getEvent().name());
            reportEventLabel.setText(user.getEvent().name());
            reloadParticipantList();
            loadReportData();
        }
    }

    public void initialize() {
        // Setup participants table
        nameColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        pointsColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleIntegerProperty(data.getValue().getTotalPoints()).asObject());
        participantTable.setItems(participantsModel);
        
        // Setup report table
        reportNameColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getParticipantName()));
        reportPointsColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleIntegerProperty(data.getValue().getPoints()).asObject());
        reportTable.setItems(reportModel);
        
        // Add selection listener for participant
        participantTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedParticipantLabel.setText(newSelection.getName() + " (Current points: " + newSelection.getTotalPoints() + ")");
            } else {
                selectedParticipantLabel.setText("No participant selected");
            }
        });
        
        // Initial state
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
        List<Participant> participants = service.getAllParticipantsWithTotalPoints();
        participantsModel.setAll(participants);
    }
    
    private void loadReportData() {
        // Sums points by participant
        List<ParticipantEventPoints> results = service.getAggregatedResultsByEvent(loggedInUser.getEvent().name());
        reportModel.setAll(results);
    }

    @FXML
    private void handleAddPoints() {
        Participant selected = participantTable.getSelectionModel().getSelectedItem();
        String pointsText = pointsInputField.getText();

        if (selected == null) {
            showError("Please select a participant from the table.");
            return;
        }
        
        if (pointsText.isEmpty()) {
            showError("Please enter points value.");
            return;
        }

        try {
            int points = Integer.parseInt(pointsText);
            
            if (points < 0) {
                showError("Points must be a positive number.");
                return;
            }
            
            // Create result with the logged-in referee's event
            Result result = new Result(selected, loggedInUser.getEvent(), points);

            service.addResult(result);

            pointsInputField.clear();

            showInformation("Successfully added " + points + " points to " + selected.getName() + ".");
        } catch (NumberFormatException e) {
            showError("Points must be a valid number.");
        }
    }

    @FXML
    private void handleViewReport() {
        // Use the aggregated method to get summed points by participant
        List<ParticipantEventPoints> reportResults = service.getAggregatedResultsByEvent(loggedInUser.getEvent().name());

        if (reportResults.isEmpty()) {
            showInformation("No results found for event: " + loggedInUser.getEvent().name());
            return;
        }

        StringBuilder reportText = new StringBuilder("Results for event: " + loggedInUser.getEvent().name() + "\n\n");
        
        for (ParticipantEventPoints result : reportResults) {
            reportText.append(result.getParticipantName())
                     .append(" - ").append(result.getPoints()).append(" points\n");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Event Report");
        alert.setHeaderText("Participants in " + loggedInUser.getEvent());
        alert.setContentText(reportText.toString());
        alert.showAndWait();
    }

    @FXML
    private void handleRefreshReport() {
        loadReportData();
    }

    @FXML
    private void handleLogout() {
        // Unregister listener when logging out
        if (service != null) {
            service.removeDataChangeListener(this);
        }
        participantTable.getScene().getWindow().hide();
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
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
    // DataChangeListener
    @Override
    public void onDataChanged() {
        reloadParticipantList();
        loadReportData();
    }
}
