package service;

import models.Participant;
import models.Result;
import models.User;

import java.util.List;
import java.util.Optional;

public interface ITriathlonService {

    // Login
    Optional<User> authenticate(String username, String password);

    // Main view: all participants with total points
    List<Participant> getAllParticipantsWithTotalPoints();

    // Add result
    void addResult(Result result);


    Participant addParticipant(String name);


    // Report: results by event (for that arbiter)
    List<Result> getResultsByEvent(String event);
}