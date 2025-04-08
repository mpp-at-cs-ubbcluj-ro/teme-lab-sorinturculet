package ro.mpp2024.repository;

import ro.mpp2024.models.Result;
import ro.mpp2024.models.Participant;
import ro.mpp2024.models.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class ResultsDBRepository implements IResultRepository {
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

    @Override
    public List<Object[]> getTotalPointsPerParticipant() {
        logger.traceEntry("Getting total points per participant");
        List<Object[]> results = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        String sql = "SELECT p.id, p.name, COALESCE(SUM(r.points), 0) as total_points " +
                     "FROM participants p " +
                     "LEFT JOIN results r ON p.id = r.participant_id " +
                     "GROUP BY p.id, p.name " +
                     "ORDER BY p.name ASC";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] result = new Object[3];
                result[0] = rs.getInt("id");
                result[1] = rs.getString("name");
                result[2] = rs.getInt("total_points");
                results.add(result);
            }
        } catch (SQLException ex) {
            logger.error("Error getting total points per participant", ex);
        }
        return logger.traceExit(results);
    }

    @Override
    public List<Result> getResultsByEvent(String event) {
        logger.traceEntry("Getting results for event: {}", event);
        List<Result> results = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        String sql = "SELECT r.*, p.name as participant_name " +
                     "FROM results r " +
                     "JOIN participants p ON r.participant_id = p.id " +
                     "WHERE r.event = ? " +
                     "ORDER BY r.points DESC";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, event);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int participantId = rs.getInt("participant_id");
                    String participantName = rs.getString("participant_name");
                    int points = rs.getInt("points");
                    
                    Participant participant = new Participant(participantName);
                    participant.setId(participantId);
                    
                    Result result = new Result(participant, Event.valueOf(event), points);
                    result.setId(id);
                    results.add(result);
                }
            }
        } catch (SQLException ex) {
            logger.error("Error getting results by event", ex);
        }
        return logger.traceExit(results);
    }
    
    /**
     * Get total points for each participant by event, summing where needed
     */
    public Map<Integer, Integer> getTotalPointsByParticipantForEvent(String event) {
        logger.traceEntry("Getting total points by participant for event: {}", event);
        Map<Integer, Integer> pointsMap = new HashMap<>();
        Connection con = dbUtils.getConnection();
        String sql = "SELECT participant_id, SUM(points) as total_points " +
                     "FROM results " +
                     "WHERE event = ? " +
                     "GROUP BY participant_id";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, event);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int participantId = rs.getInt("participant_id");
                    int totalPoints = rs.getInt("total_points");
                    pointsMap.put(participantId, totalPoints);
                }
            }
        } catch (SQLException ex) {
            logger.error("Error getting total points by participant for event", ex);
        }
        return logger.traceExit(pointsMap);
    }
}
