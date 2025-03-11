import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MainBD {
    public static void main(String[] args) {

        Properties props = new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }

        CarRepository carRepo = new CarsDBRepository(props);
        carRepo.add(new Car("Tesla", "Model S", 2019));
        System.out.println("Toate masinile din db");
        for (Car car : carRepo.findAll())
            System.out.println(car);

        String manufacturer = "Tesla";
        System.out.println("Masinile produse de " + manufacturer);
        for (Car car : carRepo.findByManufacturer(manufacturer))
            System.out.println(car);

        // Update the year of Tesla to 2023
        Car updatedCar = new Car("Tesla", "Model S", 2023);
        carRepo.update(1, updatedCar); // Assuming the ID of the Tesla car is 1

        System.out.println("Toate masinile din db dupa update");
        for (Car car : carRepo.findAll())
            System.out.println(car);
    }
}