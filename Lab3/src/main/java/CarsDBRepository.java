import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CarsDBRepository implements CarRepository{

    private JdbcUtils dbUtils;



    private static final Logger logger= LogManager.getLogger();

    public CarsDBRepository(Properties props) {
        logger.info("Initializing CarsDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public List<Car> findByManufacturer(String manufacturerN) {
 	logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Car> cars=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Cars where manufacturer=?")) {
            preStmt.setString(1, manufacturerN);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String manufacturer = result.getString("manufacturer");
                    String model = result.getString("model");
                    int year = result.getInt("year");
                    Car car=new Car(manufacturer,model,year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        return cars;
    }

    @Override
    public List<Car> findBetweenYears(int min, int max) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Car> cars = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Cars where year between ? and ?")) {
            preStmt.setInt(1, min);
            preStmt.setInt(2, max);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String manufacturer = result.getString("manufacturer");
                    String model = result.getString("model");
                    int year = result.getInt("year");
                    Car car=new Car(manufacturer,model,year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        }catch(SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        return cars;
    }

    @Override
    public void add(Car elem) {
    logger.traceEntry();
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Cars (manufacturer,model,year) values (?,?,?)")){
            preStmt.setString(1,elem.getManufacturer());
            preStmt.setString(2,elem.getModel());
            preStmt.setInt(3,elem.getYear());
            int result=preStmt.executeUpdate();
            logger.traceEntry("Saved {} instances",result);
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
    }

    @Override
    public void update(Integer integer, Car elem) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Cars set manufacturer=?, model=?, year=? where id=?")) {
            preStmt.setString(1, elem.getManufacturer());
            preStmt.setString(2, elem.getModel());
            preStmt.setInt(3, elem.getYear());
            preStmt.setInt(4, integer);
            int result = preStmt.executeUpdate();
            logger.traceEntry("Updated {} instances", result);
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
    }

    @Override
    public Iterable<Car> findAll() {
         logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Car> cars=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Cars")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String manufacturer = result.getString("manufacturer");
                    String model = result.getString("model");
                    int year = result.getInt("year");
                    Car car=new Car(manufacturer,model,year);
                    car.setId(id);
                    cars.add(car);
                }

            }
        }catch(SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
    logger.traceExit(cars);
        return cars;
    }
}
