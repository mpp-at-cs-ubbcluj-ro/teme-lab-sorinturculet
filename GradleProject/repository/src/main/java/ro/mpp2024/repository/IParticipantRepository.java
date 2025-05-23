package ro.mpp2024.repository;

import ro.mpp2024.models.Participant;

import java.util.List;

public interface IParticipantRepository extends ICrudRepository<Participant, Integer> {
    List<Participant> findAllParticipantsSorted();
    List<Participant> findByName(String name);

    void delete(Integer id);
}