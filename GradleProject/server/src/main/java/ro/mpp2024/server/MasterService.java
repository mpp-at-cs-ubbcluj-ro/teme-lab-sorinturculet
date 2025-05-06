package ro.mpp2024.server;

import ro.mpp2024.models.Participant;
import ro.mpp2024.models.ParticipantEventPoints;
import ro.mpp2024.models.Result;
import ro.mpp2024.models.User;
import ro.mpp2024.service.ITriathlonService;
import ro.mpp2024.repository.IUserRepository;
import ro.mpp2024.repository.IParticipantRepository;
import ro.mpp2024.repository.IResultRepository;
import ro.mpp2024.service.IUserObserver;

import java.util.*;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MasterService implements ITriathlonService {
    private final IUserRepository userRepo;
    private final IParticipantRepository participantRepo;
    private final IResultRepository resultRepo;
    private final Map<String, IUserObserver> observers = new ConcurrentHashMap<>();

    @Override
    public void registerObserver(IUserObserver observer, String username) {
        observers.put(username, observer);
    }

    @Override
    public void removeObserver(String username) {
        observers.remove(username);
    }

    public MasterService(IUserRepository userRepo, IParticipantRepository participantRepo, IResultRepository resultRepo) {
        this.userRepo = userRepo;
        this.participantRepo = participantRepo;
        this.resultRepo = resultRepo;
    }

    @Override
    public Optional<User> authenticate(String username, String password) {
        return userRepo.authenticate(username, password);
    }

    @Override
    public void logout() {

    }

    @Override
    public List<Participant> getAllParticipantsWithTotalPoints() {
        return participantRepo.findAllParticipantsSorted();
    }

    @Override
    public void addResult(Result result) {
        resultRepo.create(result);
        notifyClients();

    }

    @Override
    public Participant addParticipant(String name) {
        Participant newParticipant = new Participant(name);
        notifyClients();
        return participantRepo.create(newParticipant);

    }

    @Override
    public List<ParticipantEventPoints> getAggregatedResultsByEvent(String event) {
        List<Participant> allParticipants = participantRepo.findAll();
        Map<Integer, Integer> pointsByParticipant = resultRepo.getTotalPointsByParticipantForEvent(event);

        List<ParticipantEventPoints> results = new ArrayList<>();
        for (Participant p : allParticipants) {
            int points = pointsByParticipant.getOrDefault(p.getId(), 0);
            results.add(new ParticipantEventPoints(p.getName(), points, p.getId()));
        }

        results.sort((p1, p2) -> Integer.compare(p2.getPoints(), p1.getPoints()));
        return results;
    }

    @Override
    public List<Result> getResultsByEvent(String event) {
        return resultRepo.getResultsByEvent(event);
    }
    private void notifyClients() {
        for (IUserObserver observer : observers.values()) {
            try {
                observer.notifyDataChanged();
            } catch (Exception e) {
                System.err.println("Error notifying client: " + e.getMessage());
            }
        }
    }

}
