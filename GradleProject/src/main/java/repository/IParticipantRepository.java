package repository;

import models.Participant;

import java.util.List;

public interface IParticipantRepository extends ICrudRepository<Participant, Integer> {
    List<Participant> findAllParticipantsSorted();
    List<Participant> findByName(String name);
}