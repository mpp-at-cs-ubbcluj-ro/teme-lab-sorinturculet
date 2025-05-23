package ro.mpp2024.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ro.mpp2024.networking.proxy.MasterJsonProxy;
import ro.mpp2024.service.ITriathlonService;

public class Main extends Application {

    private static final String HOST = "localhost";
    private static final int PORT = 55555;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create the network proxy to communicate with the server
        ITriathlonService service = new MasterJsonProxy(HOST, PORT);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
        Scene scene = new Scene(loader.load());

        MainViewController controller = loader.getController();
        controller.setService(service);
        controller.setPrimaryStage(primaryStage);

        primaryStage.setTitle("Triathlon Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
