package repository;

import models.Result;
import models.Participant;
import models.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ResultsDBRepository implements ICrudRepository<Result, Integer> {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public ResultsDBRepository(Properties props) {
        logger.info("Initializing ResultsDBRepository with properties: {}", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Result create(Result result) {
        logger.traceEntry("Creating result: {}", result);
        Connection con = dbUtils.getConnection();
        String sql = "INSERT INTO results(participant_id, event, points) VALUES(?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, result.getParticipant().getId());
            ps.setString(2, result.getEvent().name());
            ps.setInt(3, result.getPoints());
            ps.executeUpdate();
            
            // Get the auto-generated ID
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    result.setId(id);
                    logger.info("Created result with ID: {}", id);
                } else {
                    logger.error("Failed to retrieve auto-generated ID for result");
                }
            }
        } catch (SQLException ex) {
            logger.error("Error creating result", ex);
        }
        return logger.traceExit(result);
    }

    @Override
    public Optional<Result> read(Integer id) {
        logger.traceEntry("Reading result with id: {}", id);
        Connection con = dbUtils.getConnection();
        String sql = "SELECT r.*, p.name as participant_name " +
                "FROM results r JOIN participants p ON r.participant_id = p.id " +
                "WHERE r.id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    int participantId = rs.getInt("participant_id");
                    String participantName = rs.getString("participant_name");
                    Participant participant = new Participant(participantName);
                    participant.setId(participantId);
                    
                    String eventStr = rs.getString("event");
                    Event event = Event.valueOf(eventStr);
                    int points = rs.getInt("points");
                    
                    Result result = new Result(participant, event, points);
                    result.setId(id);
                    return logger.traceExit(Optional.of(result));
                }
            }
        } catch (SQLException ex) {
            logger.error("Error reading result", ex);
        }
        return logger.traceExit(Optional.empty());
    }

    @Override
    public Result update(Result result) {
        logger.traceEntry("Updating result: {}", result);
        Connection con = dbUtils.getConnection();
        String sql = "UPDATE results SET participant_id = ?, event = ?, points = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, result.getParticipant().getId());
            ps.setString(2, result.getEvent().name());
            ps.setInt(3, result.getPoints());
            ps.setInt(4, result.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.error("Error updating result", ex);
        }
        return logger.traceExit(result);
    }

    @Override
    public List<Result> findAll() {
        logger.traceEntry("Fetching all results.");
        List<Result> results = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        String sql = "SELECT r.*, p.name as participant_name " +
                "FROM results r JOIN participants p ON r.participant_id = p.id";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
            while (rs.next()) {
                int id = rs.getInt("id");
                int participantId = rs.getInt("participant_id");
                String participantName = rs.getString("participant_name");
                
                Participant participant = new Participant(participantName);
                participant.setId(participantId);
                
                String eventStr = rs.getString("event");
                Event event = Event.valueOf(eventStr);
                int points = rs.getInt("points");
                
                Result result = new Result(participant, event, points);
                result.setId(id);
                results.add(result);
            }
        } catch (SQLException ex) {
            logger.error("Error fetching results", ex);
        }
        return logger.traceExit(results);
    }
}
