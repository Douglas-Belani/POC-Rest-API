package entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AddressTest {

    private Address address;

    @Before
    public void initialize() {
        address = new Address(1, "neighborhood", "1",
                "111111-111", "street1", "complement1", null);
    }

    @Test
    public void test_setNeighborhood_Should_Throw_IllegalArgumentException_When_Null_Neighborhood_Is_Passed() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                address.setNeighborhood(null));
        Assert.assertEquals("Neighborhood must not be blank.", thrown.getMessage());
    }

    @Test
    public void test_setNeighborhood_Should_Throw_IllegalArgumentException_When_Blank_Neighborhood_Is_Passed() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                address.setNeighborhood(""));
        Assert.assertEquals("Neighborhood must not be blank.", thrown.getMessage());
    }

    @Test
    public void
    test_setNeighborhood_Should_Throw_IllegalArgumentException_When_Neighborhood_Is_Larger_Than_120_Characters() {
       StringBuilder sb = new StringBuilder();
       while (sb.length() <= 120) {
           sb.append("a");
       }

        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                address.setNeighborhood(sb.toString()));
        Assert.assertEquals("Neighborhood must have less than 120 characters.",
                thrown.getMessage());
    }

    @Test
    public void
    test_setNeighborhood_Should_Successfully_Change_Neighborhood_Field_For_Non_Null_And_Non_Blank_Neighborhood_Field_With_Less_Than_120_Characters() {
        String newNeighborhood = "newNeighborhood";
        address.setNeighborhood(newNeighborhood);
        Assert.assertEquals(newNeighborhood, address.getNeighborhood());
    }

    @Test
    public void test_setNumber_Should_Throw_IllegalArgumentException_When_Number_Is_Null() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class,
                () ->address.setNumber(null));
        Assert.assertEquals("Number must not be blank.", thrown.getMessage());
    }

    @Test
    public void test_setNumber_Should_Throw_IllegalArgumentException_When_Number_Is_Blank() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, ()
                -> address.setNumber(""));
        Assert.assertEquals("Number must not be blank.", thrown.getMessage());
    }

    @Test
    public void test_setNumber_Should_Throw_IllegalArgumentException_When_Number_Is_Larger_Than_8_Characters() {
        StringBuilder sb = new StringBuilder();
        while(sb.length() <= 8) {
            sb.append("1");
        }

        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                address.setNumber(sb.toString()));
        Assert.assertEquals(thrown.getMessage(), "Number must have less than 8 characters.");
    }

    @Test
    public void test_setNumber_Should_Successfully_Change_Number_Field_For_Non_Null_And_Non_Blank_Number_With_Less_Than_8_Characters() {
        String newNumber = "2";
        address.setNumber(newNumber);
        Assert.assertEquals(newNumber, address.getNumber());
    }

    @Test
    public void test_setZipCode_Should_Throw_IllegalArgumentException_When_Null_Zip_Code_Is_Passed() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                address.setZipCode(null));
        Assert.assertEquals("Zip code must not be blank.", thrown.getMessage());
    }

    @Test
    public void test_setZipCode_Should_Throw_IllegalArgumentException_When_Blank_Zip_Code_Is_Passed() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                address.setZipCode(""));
        Assert.assertEquals("Zip code must not be blank.", thrown.getMessage());
    }

    @Test
    public void test_setZipCode_Should_Throw_IllegalArgumentException_When_Zip_Code_Length_Is_Different_Than_10_Characters() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                address.setZipCode("1"));
        Assert.assertEquals("Zip code must have less than 10 characters.", thrown.getMessage());
    }

    @Test
    public void test_setZipCode_Should_Successfully_Change_Zip_Code_When_Non_Null_And_Non_Blank_Zip_Code_Is_Passed_With_Less_Than_10_Characters(){
        StringBuilder sb = new StringBuilder();
        while (sb.length() <= 9) {
            sb.append(1);
        }
        address.setZipCode(sb.toString());
        Assert.assertEquals(sb.toString(), address.getZipCode());
    }

    @Test
    public void test_setStreet_Should_Throw_IllegalArgumentException_When_Null_Street_Is_Passed() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                address.setStreet(null));
        Assert.assertEquals("Street must not be blank.", thrown.getMessage());
    }

    @Test
    public void test_setStreet_Should_Throw_IllegalArgumentException_When_Blank_Street_Is_Passed() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                address.setStreet(""));
        Assert.assertEquals("Street must not be blank.", thrown.getMessage());
    }

    @Test
    public void test_setStreet_Should_Throw_IllegalArgumentException_When_Street_Is_Larger_Than_120_Characters() {
        StringBuilder sb = new StringBuilder();
        while (sb.length() <= 120) {
            sb.append("a");
        }

        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                address.setStreet(sb.toString()));
        Assert.assertEquals("Street must have less than 120 characters.", thrown.getMessage());
    }

    @Test
    public void test_setStreet_Should_Successfully_Change_Street_When_Non_Null_And_Non_Blank_Street_With_Less_Than_120_Characters_Is_Passed() {
        String newStreet = "newStreet";
        address.setStreet(newStreet);
        Assert.assertEquals(newStreet, address.getStreet());
    }

    @Test
    public void getTest_setComplement_Should_Not_Throw_IllegalArgumentException_When_Null_Complement_Is_Passed() {
        address.setComplement(null);
        Assert.assertNull(address.getComplement());
    }

    @Test
    public void test_setComplement_Should_Throw_IllegalArgumentException_When_Blank_Complement_Is_Passed() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, ()
                -> address.setComplement(""));
        Assert.assertEquals("Complement must not be blank.", thrown.getMessage());

    }

    @Test
    public void test_setComplement_Should_Throw_IllegalArgumentException_When_Complement_Is_Larger_Than_120_Characters() {
        StringBuilder sb = new StringBuilder();
        while (sb.length() <= 120) {
            sb.append("a");
        }

        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                address.setComplement(sb.toString()));
        Assert.assertEquals("Complement must have less than 120 characters.",
                thrown.getMessage());
    }

    @Test
    public void test_setComplement_Should_Successfully_Change_Complement_When_Non_Blank_Complement_With_Less_Than_120_Characters_Is_Passed() {
        String newComplement = "newComplement";
        address.setComplement(newComplement);
        Assert.assertEquals(newComplement, address.getComplement());
    }

}
