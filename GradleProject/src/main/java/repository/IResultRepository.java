package repository;

import models.Result;

import java.util.List;

public interface IResultRepository extends ICrudRepository<Result, Integer> {
    List<Object[]> getTotalPointsPerParticipant();
    List<Result> getResultsByEvent(String event);
}