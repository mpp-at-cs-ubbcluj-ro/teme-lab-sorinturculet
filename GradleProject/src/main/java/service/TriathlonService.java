package service;

import javafx.application.Platform;
import models.Participant;
import models.Result;
import models.User;
import repository.IParticipantRepository;
import repository.IResultRepository;
import repository.IUserRepository;
import repository.ResultsDBRepository;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class TriathlonService implements ITriathlonService {

    private final IUserRepository userRepo;
    private final IParticipantRepository participantRepo;
    private final IResultRepository resultRepo;
    
    // Observer pattern for real-time updates
    private final List<DataChangeListener> listeners = new CopyOnWriteArrayList<>();

    public TriathlonService(IUserRepository userRepo,
                            IParticipantRepository participantRepo,
                            IResultRepository resultRepo) {
        this.userRepo = userRepo;
        this.participantRepo = participantRepo;
        this.resultRepo = resultRepo;
    }
    
    // Interface for observers
    public interface DataChangeListener {
        void onDataChanged();
    }
    
    // Methods to register/unregister observers
    public void addDataChangeListener(DataChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public void removeDataChangeListener(DataChangeListener listener) {
        listeners.remove(listener);
    }
    
    // Method to notify all observers
    private void notifyDataChanged() {
        for (DataChangeListener listener : listeners) {
            // Use Platform.runLater to ensure UI updates happen on the JavaFX thread
            Platform.runLater(listener::onDataChanged);
        }
    }

    @Override
    public Optional<User> authenticate(String username, String password) {
        return userRepo.authenticate(username, password);
    }

    @Override
    public List<Participant> getAllParticipantsWithTotalPoints() {
        // Points are calculated directly
        return participantRepo.findAllParticipantsSorted();
    }

    @Override
    public void addResult(Result result) {

        Result createdResult = resultRepo.create(result);

        notifyDataChanged();
    }
    @Override
    public Participant addParticipant(String name) {
        Participant newParticipant = new Participant(name);
        Participant created = participantRepo.create(newParticipant);
        notifyDataChanged();
        return created;
    }

    @Override
    public List<Result> getResultsByEvent(String event) {
        return resultRepo.getResultsByEvent(event);
    }
    
    /**
     * Get aggregated results by participant for a specific event
     */
    public List<ParticipantEventPoints> getAggregatedResultsByEvent(String event) {
        // Get all participants
        List<Participant> allParticipants = participantRepo.findAll();
        
        // Get total points
        Map<Integer, Integer> pointsByParticipant = resultRepo.getTotalPointsByParticipantForEvent(event);
        
        // Create the aggregated list
        List<ParticipantEventPoints> results = new ArrayList<>();
        
        for (Participant p : allParticipants) {
            int points = pointsByParticipant.getOrDefault(p.getId(), 0);
                results.add(new ParticipantEventPoints(p.getName(), points, p.getId()));

        }
        // Sort by points descending
        results.sort((p1, p2) -> Integer.compare(p2.getPoints(), p1.getPoints()));
        
        return results;
    }

    // Data class for aggregated participant points
    public static class ParticipantEventPoints {
        private final String participantName;
        private final int points;
        private final int participantId;
        
        public ParticipantEventPoints(String participantName, int points, int participantId) {
            this.participantName = participantName;
            this.points = points;
            this.participantId = participantId;
        }
        
        public String getParticipantName() { return participantName; }
        public int getPoints() { return points; }
        public int getParticipantId() { return participantId; }
    }
}
