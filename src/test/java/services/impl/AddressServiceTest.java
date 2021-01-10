package services.impl;

import dao.IAddressDao;
import dao.ICityDao;
import dao.IStateDao;
import entities.Address;
import entities.City;
import entities.State;
import entities.User;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import resources.exception.ResourceNotFoundException;
import services.IAddressService;
import services.exceptions.UnauthorizedException;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AddressServiceTest {

    @Mock
    private Connection conn;

    @Mock
    private IAddressDao addressDao;

    @Mock
    private ICityDao cityDao;

    @Mock
    private IStateDao stateDao;

    private IAddressService addressService;

    @Before
    public void initialize() {
        this.addressService = new AddressServiceImpl(conn, addressDao, cityDao, stateDao);
    }

    @Test
    public void test_getAddressById_Should_Throw_UnauthorizedException_For_Different_User_Address_Id() {
        User user1 = new User(1, "fullName", "111111111-11",
                "email@gmail.com", "password", LocalDate.now());
        State state = new State(1, "SP");
        City city = new City(2, "City2", state);
        Address address1 = new Address(1, "neighborhood1", "1",
                "111111-111", "street1", null, city);
        Address address2 = new Address(2, "neighborhood2", "2",
                "222222-222", "street2", "complement2", city);

        List<Address> user1Addresses = Arrays.asList(address1, address2);
        Mockito.when(addressDao.getAllAddressesByUserId(user1.getUserId())).thenReturn(user1Addresses);

        int differentAddressId = 4;
        State differentState = new State(2, "RS");
        City differentCity = new City(4, "city4", differentState);
        Address notUser1Address = new Address(differentAddressId, "differentNeighborhood", "20",
                "444444-444", "differentStreet", "differentComplement",
                differentCity);
        Mockito.when(addressDao.getAddressById(differentAddressId)).thenReturn(notUser1Address);

        UnauthorizedException thrown = Assert.assertThrows(UnauthorizedException.class, () ->
                addressService.getAddressById(differentAddressId, user1));
        Assert.assertEquals("Can't access another user's information.", thrown.getMessage());
    }

    @Test
    public void test_getAddressById_Should_Return_Address_For_User_Correct_Address_Id() {
        User user1 = new User(1, "fullName", "111111111-11",
                "email@gmail.com", "password", LocalDate.now());
        State state = new State(1, "SP");
        City city = new City(2, "City2", state);
        Address address1 = new Address(1, "neighborhood1", "1",
                "111111-111", "street1", null, city);
        Address address2 = new Address(2, "neighborhood2", "2",
                "222222-222", "street2", "complement2", city);

        List<Address> user1Addresses = Arrays.asList(address1, address2);
        Mockito.when(addressDao.getAllAddressesByUserId(user1.getUserId())).thenReturn(user1Addresses);
        Mockito.when(addressDao.getAddressById(address1.getAddressId())).thenReturn(address1);

        Address expectedAddress = addressService.getAddressById(address1.getAddressId(), user1);
        assertThat(address1, samePropertyValuesAs(expectedAddress));
    }

    @Test
    public void test_insertAddress_Should_Create_New_State_And_City_If_State_Is_Not_Found() throws SQLException {
        User user = new User(1, "fullName", "111111111-11", "email@gmail.com",
                "passworD@2", LocalDate.now());
        State state = new State(null, "SP");
        City city = new City(null, "city1", state);
        Address address = new Address(1, "neighborhood1",
                "11", "111111111-11", "street1",
                "complement1", city);

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(stateDao.getStateByInitials(state.getInitials())).thenReturn(null);
        Mockito.when(cityDao.getCityByNameAndStateInitials(city.getName(), state.getInitials()))
                .thenReturn(null);
        Mockito.when(stateDao.insertState(state)).thenReturn(1);
        Mockito.when(cityDao.insertCity(city)).thenReturn(2);
        Mockito.when(addressDao.insertAddress(address, user.getUserId())).thenReturn(2);
        Mockito.doNothing().when(conn).commit();
        Assert.assertEquals(Integer.valueOf(2), addressService.insertAddress(address, user));
        Assert.assertEquals(Integer.valueOf(1), state.getStateId());
        Assert.assertEquals(Integer.valueOf(2), city.getCityId());
        Mockito.verify(conn, Mockito.times(1)).commit();

    }

    @Test
    public void test_insertAddress_Should_Create_New_City_If_City_Is_Not_Found() throws SQLException {
        User user = new User(1, "fullName", "111111111-11", "email@gmail.com",
                "passworD@2", LocalDate.now());
        State state = new State(1, "SP");
        City city = new City(null, "city1", state);
        Address address = new Address(1, "neighborhood1",
                "11", "111111111-11", "street1",
                "complement1", city);

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(stateDao.getStateByInitials(state.getInitials())).thenReturn(state);
        Mockito.when(cityDao.getCityByNameAndStateInitials(city.getName(), state.getInitials()))
                .thenReturn(null);
        Mockito.when(cityDao.insertCity(city)).thenReturn(2);
        Mockito.when(addressDao.insertAddress(address, user.getUserId())).thenReturn(2);
        Mockito.doNothing().when(conn).commit();
        Assert.assertEquals(Integer.valueOf(2), addressService.insertAddress(address, user));
        Assert.assertEquals(Integer.valueOf(1), state.getStateId());
        Assert.assertEquals(Integer.valueOf(2), city.getCityId());
        Mockito.verify(conn, Mockito.times(1)).commit();
    }

    @Test
    public void test_insertAddress_Should_Return_Address_Id_When_State_And_City_Are_Found() throws SQLException {
        User user = new User(1, "fullName", "111111111-11", "email@gmail.com",
                "passworD@2", LocalDate.now());
        State state = new State(1, "SP");
        City city = new City(2, "city1", state);
        Address address = new Address(1, "neighborhood1",
                "11", "111111111-11", "street1",
                "complement1", city);

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(stateDao.getStateByInitials(state.getInitials())).thenReturn(state);
        Mockito.when(cityDao.getCityByNameAndStateInitials(city.getName(), state.getInitials()))
                .thenReturn(city);
        Mockito.when(addressDao.insertAddress(address, user.getUserId())).thenReturn(2);
        Mockito.doNothing().when(conn).commit();
        Assert.assertEquals(Integer.valueOf(2), addressService.insertAddress(address, user));
        Assert.assertEquals(Integer.valueOf(1), state.getStateId());
        Assert.assertEquals(Integer.valueOf(2), city.getCityId());
        Mockito.verify(conn, Mockito.times(1)).commit();

    }

    @Test
    public void test_insertAddress_Should_Return_Null_If_State_Is_Not_Found_And_Insert_State_Returns_Null() throws SQLException {
        User user = new User(1, "fullName", "111111111-11", "email@gmail.com",
                "passworD@2", LocalDate.now());
        State state = new State(null, "SP");
        City city = new City(null, "city1", state);
        Address address = new Address(1, "neighborhood1",
                "11", "111111111-11", "street1",
                "complement1", city);

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(stateDao.getStateByInitials(state.getInitials())).thenReturn(null);
        Mockito.when(cityDao.getCityByNameAndStateInitials(city.getName(), state.getInitials()))
                .thenReturn(null);
        Mockito.when(stateDao.insertState(state)).thenReturn(null);
        Assert.assertNull(addressService.insertAddress(address, user));
        Mockito.verify(conn, Mockito.times(1)).rollback();
    }

    @Test
    public void test_insertAddress_Should_Return_Null_If_State_Is_Not_Found_And_Insert_City_Returns_Null() throws SQLException {
        User user = new User(1, "fullName", "111111111-11", "email@gmail.com",
                "passworD@2", LocalDate.now());
        State state = new State(null, "SP");
        City city = new City(null, "city1", state);
        Address address = new Address(1, "neighborhood1",
                "11", "111111111-11", "street1",
                "complement1", city);

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(stateDao.getStateByInitials(state.getInitials())).thenReturn(null);
        Mockito.when(cityDao.getCityByNameAndStateInitials(city.getName(), state.getInitials()))
                .thenReturn(null);
        Mockito.when(stateDao.insertState(state)).thenReturn(1);
        Mockito.when(cityDao.insertCity(city)).thenReturn(null);
        Assert.assertNull(addressService.insertAddress(address, user));
        Assert.assertEquals(Integer.valueOf(1), state.getStateId());
        Mockito.verify(conn, Mockito.times(1)).rollback();
    }

    @Test
    public void test_insertAddress_Should_Return_Null_If_City_Is_Not_Found_And_Insert_City_Returns_Null() throws SQLException {
        User user = new User(1, "fullName", "111111111-11", "email@gmail.com",
                "passworD@2", LocalDate.now());
        State state = new State(1, "SP");
        City city = new City(null, "city1", state);
        Address address = new Address(1, "neighborhood1",
                "11", "111111111-11", "street1",
                "complement1", city);

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(stateDao.getStateByInitials(state.getInitials())).thenReturn(state);
        Mockito.when(cityDao.getCityByNameAndStateInitials(city.getName(), state.getInitials()))
                .thenReturn(null);
        Mockito.when(cityDao.insertCity(city)).thenReturn(null);
        Assert.assertNull(addressService.insertAddress(address, user));
        Mockito.verify(conn, Mockito.times(1)).rollback();
    }

    @Test
    public void test_insertAddress_Should_Return_Null_If_InsertAddress_Returns_Address_Id_Null() throws SQLException {
        User user = new User(1, "fullName", "111111111-11", "email@gmail.com",
                "passworD@2", LocalDate.now());
        State state = new State(1, "SP");
        City city = new City(null, "city1", state);
        Address address = new Address(1, "neighborhood1",
                "11", "111111111-11", "street1",
                "complement1", city);

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(stateDao.getStateByInitials(state.getInitials())).thenReturn(state);
        Mockito.when(cityDao.getCityByNameAndStateInitials(city.getName(), state.getInitials()))
                .thenReturn(city);
        Mockito.when(addressDao.insertAddress(address, user.getUserId())).thenReturn(null);
        Assert.assertNull(addressService.insertAddress(address, user));
        Mockito.verify(conn, Mockito.times(1)).rollback();
    }

    @Test
    public void test_updateAddress_Should_Return_True_For_Successfully_Updated_Address() throws SQLException {
        int id = 1;
        User user = new User(1, "fullName", "111111111-11", "email@gmail.com",
                "passworD@2", LocalDate.now());
        State state = new State(1, "SP");
        City city = new City(2, "city1", state);
        Address address = new Address(1, "neighborhood1",
                "11", "111111111-11", "street1",
                "complement1", city);
        Address updatedAddress = new Address(null, "updatedNeighborhood1",
                "2", "222222222-22", "updatedStreet",
                "updatedComplement", city);

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(addressDao.getAllAddressesByUserId(user.getUserId())).thenReturn(Arrays.asList(address));
        Mockito.when(addressDao.getAddressById(id)).thenReturn(address);
        Mockito.when(stateDao.getStateByInitials(state.getInitials())).thenReturn(state);
        Mockito.when(cityDao.getCityByNameAndStateInitials(city.getName(), state.getInitials())).thenReturn(city);
        Mockito.when(addressDao.updateAddress(updatedAddress)).thenReturn(true);
        Mockito.doNothing().when(conn).commit();
        Assert.assertTrue(addressService.updateAddress(id, updatedAddress, user));
        Assert.assertEquals(Integer.valueOf(1), updatedAddress.getAddressId());
        Mockito.verify(conn, Mockito.times(1)).commit();
    }

    @Test
    public void test_updateAddress_Should_Throw_ResourceNotFoundException_If_Nonexistent_Is_Passed() throws SQLException {
        int nonexistentAddressId = 899;
        User user = new User(1, "fullName", "111111111-11", "email@gmail.com",
                "passworD@2", LocalDate.now());
        State state = new State(1, "SP");
        City city = new City(2, "city1", state);
        Address address = new Address(1, "neighborhood1",
                "11", "111111111-11", "street1",
                "complement1", city);
        Address updatedAddress = new Address(null, "updatedNeighborhood1",
                "2", "222222222-22", "updatedStreet",
                "updatedComplement", city);

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(addressDao.getAllAddressesByUserId(user.getUserId())).thenReturn(Arrays.asList(address));
        Mockito.when(addressDao.getAddressById(nonexistentAddressId)).thenReturn(null);
        ResourceNotFoundException thrown = Assert.assertThrows(ResourceNotFoundException.class, () ->
                addressService.updateAddress(nonexistentAddressId, updatedAddress, user));
        Assert.assertEquals("Address id " + nonexistentAddressId + " not found.", thrown.getMessage());
    }

    @Test
    public void test_updateAddress_Should_Throw_UnauthorizedException_For_Wrong_User() throws SQLException {
        int id = 1;
        User user = new User(1, "fullName", "111111111-11", "email@gmail.com",
                "passworD@2", LocalDate.now());
        State state = new State(1, "SP");
        City city = new City(2, "city1", state);
        Address address = new Address(1, "neighborhood1",
                "11", "111111111-11", "street1",
                "complement1", city);
        Address updatedAddress = new Address(null, "updatedNeighborhood1",
                "2", "222222222-22", "updatedStreet",
                "updatedComplement", city);

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(addressDao.getAllAddressesByUserId(user.getUserId())).thenReturn(new ArrayList<Address>());
        Mockito.when(addressDao.getAddressById(id)).thenReturn(address);
        UnauthorizedException thronw = Assert.assertThrows(UnauthorizedException.class, () ->
                addressService.updateAddress(id, address, user));
        Assert.assertEquals("Can't access another user's information.",
                thronw.getMessage());
    }

    @Test
    public void test_updatedAddress_Should_Successfully_Insert_New_State_And_City_If_State_Is_Not_Found_And_Return_True() throws SQLException {
        int id = 1;
        User user = new User(1, "fullName", "111111111-11", "email@gmail.com",
                "passworD@2", LocalDate.now());
        State state = new State(null, "SP");
        City city = new City(null, "city1", state);
        Address address = new Address(1, "neighborhood1",
                "11", "111111111-11", "street1",
                "complement1", city);
        Address updatedAddress = new Address(null, "updatedNeighborhood1",
                "2", "222222222-22", "updatedStreet",
                "updatedComplement", city);

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(addressDao.getAddressById(id)).thenReturn(address);
        Mockito.when(addressDao.getAllAddressesByUserId(user.getUserId()))
                .thenReturn(Arrays.asList(address));
        Mockito.when(stateDao.getStateByInitials(state.getInitials())).thenReturn(null);
        Mockito.when(cityDao.getCityByNameAndStateInitials(city.getName(),
                state.getInitials())).thenReturn(null);
        Mockito.when(stateDao.insertState(state)).thenReturn(2);
        Mockito.when(cityDao.insertCity(city)).thenReturn(1);
        Mockito.when(addressDao.updateAddress(updatedAddress)).thenReturn(true);
        Mockito.doNothing().when(conn).commit();
        Assert.assertTrue(addressService.updateAddress(id, updatedAddress, user));
        Assert.assertEquals(Integer.valueOf(2), state.getStateId());
        Assert.assertEquals(Integer.valueOf(1), city.getCityId());
        Mockito.verify(conn, Mockito.times(1)).commit();
    }

    @Test
    public void test_updateAddress_Should_Successfully_Insert_New_City_If_City_Is_Not_Found_And_Return_True_If_City_Is_Not_Found() throws SQLException {
        int id = 1;
        User user = new User(1, "fullName", "111111111-11", "email@gmail.com",
                "passworD@2", LocalDate.now());
        State state = new State(2, "SP");
        City city = new City(null, "city1", state);
        Address address = new Address(1, "neighborhood1",
                "11", "111111111-11", "street1",
                "complement1", city);
        Address updatedAddress = new Address(null, "updatedNeighborhood1",
                "2", "222222222-22", "updatedStreet",
                "updatedComplement", city);

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(addressDao.getAddressById(id)).thenReturn(address);
        Mockito.when(addressDao.getAllAddressesByUserId(user.getUserId()))
                .thenReturn(Arrays.asList(address));
        Mockito.when(stateDao.getStateByInitials(state.getInitials())).thenReturn(state);
        Mockito.when(cityDao.getCityByNameAndStateInitials(city.getName(),
                state.getInitials())).thenReturn(null);
        Mockito.when(cityDao.insertCity(city)).thenReturn(1);
        Mockito.when(addressDao.updateAddress(updatedAddress)).thenReturn(true);
        Mockito.doNothing().when(conn).commit();
        Assert.assertTrue(addressService.updateAddress(id, updatedAddress, user));
        Assert.assertEquals(Integer.valueOf(1), city.getCityId());
        Mockito.verify(conn, Mockito.times(1)).commit();
    }

    @Test
    public void test_updateAddress_Should_Return_False_If_AddressDao_UpdateAddress_Returns_False() throws SQLException{
        int id = 1;
        User user = new User(1, "fullName", "111111111-11", "email@gmail.com",
                "passworD@2", LocalDate.now());
        State state = new State(2, "SP");
        City city = new City(1, "city1", state);
        Address address = new Address(1, "neighborhood1",
                "11", "111111111-11", "street1",
                "complement1", city);
        Address updatedAddress = new Address(null, "updatedNeighborhood1",
                "2", "222222222-22", "updatedStreet",
                "updatedComplement", city);

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(addressDao.getAddressById(id)).thenReturn(address);
        Mockito.when(addressDao.getAllAddressesByUserId(user.getUserId()))
                .thenReturn(Arrays.asList(address));
        Mockito.when(stateDao.getStateByInitials(state.getInitials())).thenReturn(state);
        Mockito.when(cityDao.getCityByNameAndStateInitials(city.getName(),
                state.getInitials())).thenReturn(city);
        Mockito.when(addressDao.updateAddress(updatedAddress)).thenReturn(false);
        Mockito.doNothing().when(conn).rollback();
        Assert.assertFalse(addressService.updateAddress(id, updatedAddress, user));
        Assert.assertEquals(Integer.valueOf(1), city.getCityId());
        Mockito.verify(conn, Mockito.times(1)).rollback();
    }

    @Test
    public void test_deleteAddress_Should_Throw_UnauthorizedException_For_Invalid_User_Id() {
        int addressId = 1;
        User user = new User(1, "fullName", "111111111-11", "email@gmail.com",
                "passworD@2", LocalDate.now());
        State state = new State(2, "SP");
        City city = new City(1, "city1", state);
        Address address = new Address(1, "neighborhood1",
                "11", "111111111-11", "street1",
                "complement1", city);
        Mockito.when(addressDao.getAddressById(addressId)).thenReturn(address);
        Mockito.when(addressDao.getAllAddressesByUserId(user.getUserId())).thenReturn(new ArrayList<>());
        UnauthorizedException thrown = Assert.assertThrows(UnauthorizedException.class, () ->
                addressService.deleteAddress(addressId, user));
        Assert.assertEquals("Can't access another user's information.", thrown.getMessage());
    }
}
