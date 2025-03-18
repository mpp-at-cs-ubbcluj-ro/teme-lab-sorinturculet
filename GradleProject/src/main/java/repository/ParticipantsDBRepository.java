package repository;

import models.Participant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ParticipantsDBRepository implements ICrudRepository<Participant, Integer> {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public ParticipantsDBRepository(Properties props) {
        logger.info("Initializing ParticipantsDBRepository with properties: {}", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Participant create(Participant participant) {
        logger.traceEntry("Creating participant: {}", participant);
        Connection con = dbUtils.getConnection();
        String sql = "INSERT INTO participants(name) VALUES(?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, participant.getName());
            ps.executeUpdate();
            
            // Get the auto-generated ID
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    participant.setId(id);
                    logger.info("Created participant with ID: {}", id);
                } else {
                    logger.error("Failed to retrieve auto-generated ID for participant");
                }
            }
        } catch (SQLException ex) {
            logger.error("Error creating participant", ex);
        }
        return logger.traceExit(participant);
    }

    @Override
    public Optional<Participant> read(Integer id) {
        logger.traceEntry("Reading participant with id: {}", id);
        Connection con = dbUtils.getConnection();
        String sql = "SELECT * FROM participants WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    Participant participant = new Participant(name);
                    participant.setId(id);
                    return logger.traceExit(Optional.of(participant));
                }
            }
        } catch (SQLException ex) {
            logger.error("Error reading participant", ex);
        }
        return logger.traceExit(Optional.empty());
    }

    @Override
    public Participant update(Participant participant) {
        logger.traceEntry("Updating participant: {}", participant);
        Connection con = dbUtils.getConnection();
        String sql = "UPDATE participants SET name = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, participant.getName());
            ps.setInt(2, participant.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.error("Error updating participant", ex);
        }
        return logger.traceExit(participant);
    }

    @Override
    public List<Participant> findAll() {
        logger.traceEntry("Fetching all participants ordered by name.");
        List<Participant> participants = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        String sql = "SELECT * FROM participants ORDER BY name ASC";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Participant participant = new Participant(name);
                participant.setId(id);
                participants.add(participant);
            }
        } catch (SQLException ex) {
            logger.error("Error fetching participants", ex);
        }
        return logger.traceExit(participants);
    }
}
