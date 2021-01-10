package dao.impl;

import connection.H2Connection;
import dao.IAddressDao;
import entities.Address;
import entities.City;
import entities.State;
import entities.User;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

public class AddressDaoImplTest {

    private static IAddressDao addressDao;

    @BeforeClass
    public static void initialize() {
        addressDao = new AddressDaoImpl(H2Connection.getH2DatabaseConnection());

        }


    @Test
    public void test_GetAllAddressesByUser_Should_Return_Addresses_Id_1_2_For_User_Id_1() {
        User user = new User(1, "user1", "111111111-11",
                "user1@gmai.com","user1Password",
                LocalDate.of(2005, 8, 10));
        State stateExpected = new State(1, "SP");

        City cityExpected1 = new City(1, "city1", stateExpected);
        City cityExpected2 = new City(2, "city2", stateExpected);

        Address addressExpected1 = new Address(1, "neighborhood1",
                "1", "111111-111", "street1",
                "complement1", cityExpected1);
        Address addressExpected2 = new Address(2, "neighborhood2",
                "2", "222222-222", "street2",
                "complement2", cityExpected2);

        List<Address> addresses = addressDao.getAllAddressesByUserId(user.getUserId());

        Assert.assertEquals(2, addresses.size());
        assertThat(addressExpected1, samePropertyValuesAs(addresses.get(0)));
        assertThat(addressExpected2, samePropertyValuesAs(addresses.get(1)));
    }

    @Test
    public void test_GetAllAddressesByUser_Should_Return_Empty_AddressesList_For_User_Id_4() {
        User user = new User(4, "user4", "444444444-44",
                "user4@gmail.com", "user4Password",
                LocalDate.of(2003, 4, 3));

        List<Address> addresses = addressDao.getAllAddressesByUserId(user.getUserId());

        Assert.assertEquals(0, addresses.size());
    }

    @Test
    public void test_GetAddressById_Should_Return_Address_Id_3() {
        State expectedState = new State(1, "SP");

        City expectedCity = new City(2, "city2", expectedState);

        Address expectedAddress = new Address(3, "neighborhood3",
                "3", "333333-333", "street3",
                "complement3", expectedCity);

        Address address = addressDao.getAddressById(3);
        assertThat(expectedAddress, samePropertyValuesAs(address));

    }

    @Test
    public void test_GetAddressById_Should_Return_Null_For_Nonexistent_Address_Id() {
        Address address = addressDao.getAddressById(6);

        Assert.assertNull(address);
    }

    @Test
    public void test_insertAddress_Should_Return_Address_Id_7_On_Successful_Insert() {
        State state = new State(3, "RS");
        City city = new City(4, "city4", state);
        Address newAddress = new Address(null, "neighborhood7",
                "7", "777777-777", "street7",
                "complement7", city);

        int userId = 2;

        int addressId = addressDao.insertAddress(newAddress, userId);

        Assert.assertEquals(7, addressId);
        newAddress.setAddressId(addressId);

        Address insertedAddress = addressDao.getAddressById(7);

        assertThat(newAddress, samePropertyValuesAs(insertedAddress));
    }

    @Test
    public void test_UpdateAddress_Should_Return_True_On_Successful_Update() {
        Address oldAddress = addressDao.getAddressById(5);
        oldAddress.setNeighborhood("neighborhood6");
        oldAddress.setNumber("6");
        oldAddress.setZipCode("666666-666");
        oldAddress.setStreet("street6");
        oldAddress.setComplement("complement6");

        boolean updated = addressDao.updateAddress(oldAddress);
        Assert.assertTrue(updated);

        Address newAddress = addressDao.getAddressById(5);
        assertThat(oldAddress, samePropertyValuesAs(newAddress));

    }

    @Test
    public void test_UpdateAddress_Should_Return_False_When_Nonexistent_Address_Id_Is_Passed() {
        Address oldAddress = addressDao.getAddressById(5);
        oldAddress.setAddressId(10);
        oldAddress.setNeighborhood("neighborhood6");
        oldAddress.setNumber("6");
        oldAddress.setZipCode("666666-666");
        oldAddress.setStreet("street6");
        oldAddress.setComplement("complement6");

        boolean updated = addressDao.updateAddress(oldAddress);

        Assert.assertFalse(updated);
    }

    @Test
    public void test_DeleteAddressById_Should_Return_True_On_Successful_Delete() {
        int addressId = 6;
        boolean deleted = addressDao.deleteAddressById(addressId);

        Assert.assertTrue(deleted);
    }

    @Test
    public void test_DeleteAddressById_Should_Return_False_When_Nonexistent_Address_Id_Is_Passed() {
        int addressId = 8;
        boolean deleted = addressDao.deleteAddressById(addressId);
        Assert.assertFalse(deleted);
    }

}
