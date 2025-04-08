package ro.mpp2024.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ro.mpp2024.models.User;
import ro.mpp2024.service.ITriathlonService;

import java.io.IOException;
import java.util.Optional;

public class MainViewController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private ITriathlonService service;
    private Stage primaryStage;

    public void setService(ITriathlonService service) {
        this.service = service;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showErrorMessage("Please enter both username and password");
            return;
        }

        try {
            Optional<User> loggedInUser = service.authenticate(username, password);

            if (loggedInUser.isPresent()) {
                openMainApplication(loggedInUser.get());
            } else {
                showErrorMessage("Invalid username or password");
            }
        } catch (Exception e) {
            showErrorMessage("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openMainApplication(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardView.fxml"));
            Parent root = loader.load();

            DashboardViewController controller = loader.getController();
            controller.setService(service);
            controller.setUser(user);
            controller.initData();

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Triathlon Management - Welcome " + user.getName());
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            showErrorMessage("Error loading dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
