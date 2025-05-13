
import ro.mpp2024.networking.server.JsonConcurrentServer;
import ro.mpp2024.repository.*;
import ro.mpp2024.server.MasterService;
import ro.mpp2024.service.ITriathlonService;

import java.io.IOException;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        System.out.println("Reading database configuration...");

        Properties props = new Properties();
        try {
            props.load(Main.class.getResourceAsStream("/bd.config"));
        } catch (IOException e) {
            System.err.println("Error loading bd.config: " + e.getMessage());
            return;
        }

        // Initialize repositories
        IUserRepository userRepo = new UserHibernateRepository();
        IParticipantRepository participantRepo = new ParticipantHibernateRepository();
        IResultRepository resultRepo = new ResultHibernateRepository();

        // Create service
        ITriathlonService service = new MasterService(userRepo, participantRepo, resultRepo);

        // Start server
        int port = 55555;
        JsonConcurrentServer server = new JsonConcurrentServer(port, service);
        try {
            System.out.println("Starting server on port " + port + "...");
            server.start();
        } catch (Exception e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
