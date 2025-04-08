package ro.mpp2024.service;

import ro.mpp2024.models.Participant;
import ro.mpp2024.models.ParticipantEventPoints;
import ro.mpp2024.models.Result;
import ro.mpp2024.models.User;

import java.util.List;
import java.util.Optional;

public interface ITriathlonService {

    // Login
    Optional<User> authenticate(String username, String password) throws Exception;

    // Logout
    void logout() throws Exception;

    // Main view: all participants with total points
    List<Participant> getAllParticipantsWithTotalPoints() throws Exception;

    // Add result
    void addResult(Result result) throws Exception;

    // Add participant
    Participant addParticipant(String name) throws Exception;

    // Aggregated points report
    List<ParticipantEventPoints> getAggregatedResultsByEvent(String event) throws Exception;

    // Raw result report (by event)
    List<Result> getResultsByEvent(String event) throws Exception;

    // Get all events
    void registerObserver(IUserObserver observer, String username);
    void removeObserver(String username);

}