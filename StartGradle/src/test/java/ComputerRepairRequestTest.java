import model.ComputerRepairRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ComputerRepairRequestTest {

    @Test
    @DisplayName("Test getters and setters")
    public void testGettersAndSetters() {
        ComputerRepairRequest request = new ComputerRepairRequest();
        request.setID(1);
        request.setOwnerName("John Doe");
        request.setOwnerAddress("123 Main St");
        request.setPhoneNumber("555-1234");
        request.setModel("Dell");
        request.setDate("2023-10-01");
        request.setProblemDescription("Battery issue");

        assertEquals(1, request.getID());
        assertEquals("John Doe", request.getOwnerName());
        assertEquals("123 Main St", request.getOwnerAddress());
        assertEquals("555-1234", request.getPhoneNumber());
        assertEquals("Dell", request.getModel());
        assertEquals("2023-10-01", request.getDate());
        assertEquals("Battery issue", request.getProblemDescription());
    }

    @Test
    @DisplayName("Test toString method")
    public void testToString() {
        ComputerRepairRequest request = new ComputerRepairRequest(1, "John Doe", "123 Main St", "555-1234", "Dell", "2023-10-01", "Battery issue");
        String expected = "ID=1, ownerName='John Doe', model='Dell', date='2023-10-01', problemDescription='Battery issue'";
        assertEquals(expected, request.toString());
    }

}
