import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Event;
import models.Participant;
import models.Result;
import repository.ParticipantsDBRepository;
import repository.ResultsDBRepository;
import repository.UsersDBRepository;
import service.TriathlonService;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class TriathlonMain extends Application {

    private static Properties props;
    private static TriathlonService service;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Load properties
        props = new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
            return;
        }

        // Initialize repositories and service
        UsersDBRepository userRepo = new UsersDBRepository(props);
        ParticipantsDBRepository participantRepo = new ParticipantsDBRepository(props);
        ResultsDBRepository resultRepo = new ResultsDBRepository(props);

        // Create service with repositories - save to static field
        service = new TriathlonService(userRepo, participantRepo, resultRepo);

        // Load the login view
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/MainView.fxml"));
            Parent root = loader.load();
            
            // Get controller and set service
            triathlon.MainViewController controller = loader.getController();
            controller.setService(service);
            controller.setPrimaryStage(primaryStage);
            
            // Setup and show primary stage
            primaryStage.setTitle("Triathlon Login");
            primaryStage.setScene(new Scene(root, 400, 200));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static TriathlonService getService() {
        return service;
    }
}
