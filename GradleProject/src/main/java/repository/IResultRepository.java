package repository;

import models.Result;

import java.util.List;
import java.util.Map;

public interface IResultRepository extends ICrudRepository<Result, Integer> {
    List<Object[]> getTotalPointsPerParticipant();
    List<Result> getResultsByEvent(String event);
    Map<Integer, Integer> getTotalPointsByParticipantForEvent(String event);
}