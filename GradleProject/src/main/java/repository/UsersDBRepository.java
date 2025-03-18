package repository;

import models.User;
import models.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.*;
import java.util.Optional;
import java.util.List;
import java.util.Properties;

public class UsersDBRepository implements ICrudRepository<User, Integer> {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public UsersDBRepository(Properties props) {
        logger.info("Initializing UsersDBRepository with properties: {}", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public User create(User entity) {
        logger.traceEntry("Creating user: {}", entity);
        Connection con = dbUtils.getConnection();
        String sql = "INSERT INTO users(name, password, role) VALUES(?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getPassword());
            ps.setString(3, entity.getRole().name());
            ps.executeUpdate();
            
            // Get the auto-generated ID
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    entity.setId(id);
                    logger.info("Created user with ID: {}", id);
                } else {
                    logger.error("Failed to retrieve auto-generated ID for user");
                }
            }
        } catch (SQLException ex) {
            logger.error("Error creating user", ex);
        }
        return logger.traceExit(entity);
    }

    @Override
    public Optional<User> read(Integer id) {
        logger.traceEntry("Reading user with id: {}", id);
        Connection con = dbUtils.getConnection();
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    String name = rs.getString("name");
                    String password = rs.getString("password");
                    String roleStr = rs.getString("role");
                    Role role = Role.valueOf(roleStr);
                    User user = new User(name, password, role);
                    user.setId(id);
                    return logger.traceExit(Optional.of(user));
                }
            }
        } catch (SQLException ex) {
            logger.error("Error reading user", ex);
        }
        return logger.traceExit(Optional.empty());
    }

    @Override
    public User update(User entity) {
        logger.traceEntry("Updating user: {}", entity);
        Connection con = dbUtils.getConnection();
        String sql = "UPDATE users SET name = ?, password = ?, role = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getPassword());
            ps.setString(3, entity.getRole().name());
            ps.setInt(4, entity.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.error("Error updating user", ex);
        }
        return logger.traceExit(entity);
    }

    @Override
    public List<User> findAll() {
        logger.traceEntry("Finding all users.");
        List<User> users = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        String sql = "SELECT * FROM users";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String roleStr = rs.getString("role");
                Role role = Role.valueOf(roleStr);
                User user = new User(name, password, role);
                user.setId(id);
                users.add(user);
            }
        } catch (SQLException ex) {
            logger.error("Error fetching users", ex);
        }
        return logger.traceExit(users);
    }
}
