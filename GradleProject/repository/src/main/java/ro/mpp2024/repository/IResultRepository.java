package ro.mpp2024.repository;

import ro.mpp2024.models.Result;

import java.util.List;
import java.util.Map;

public interface IResultRepository extends ICrudRepository<Result, Integer> {
    List<Object[]> getTotalPointsPerParticipant();
    List<Result> getResultsByEvent(String event);
    Map<Integer, Integer> getTotalPointsByParticipantForEvent(String event);
}