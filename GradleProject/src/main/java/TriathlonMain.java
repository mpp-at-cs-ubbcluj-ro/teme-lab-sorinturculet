import models.Event;
import models.Participant;
import models.Result;
import repository.ParticipantsDBRepository;
import repository.ResultsDBRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

public class TriathlonMain {

    
    public static void main(String[] args) {
        Properties props = new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
            return;
        }


        
        ParticipantsDBRepository participantRepo = new ParticipantsDBRepository(props);
        ResultsDBRepository resultRepo = new ResultsDBRepository(props);

        Participant participant1 = new Participant("Alice");
        Participant participant2 = new Participant("Smecherul200");

        participantRepo.create(participant1);
        participantRepo.create(participant2);

        System.out.println("Participants in the system:");
        List<Participant> participants = participantRepo.findAll();
        for (Participant p : participants) {
            System.out.println("ID: " + p.getId() + ", Name: " + p.getName());
        }

        Result result1 = new Result(participant1, Event.SWIMMING, 15);

        resultRepo.create(result1);

        System.out.println("\nResults in the system:");
        List<Result> results = resultRepo.findAll();
        for (Result r : results) {
            System.out.println("Result ID: " + r.getId() +
                    ", Participant: " + r.getParticipant().getName() +
                    ", Event: " + r.getEvent() +
                    ", Points: " + r.getPoints());
        }
    }
}
