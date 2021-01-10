package services;

import entities.Address;
import entities.User;

import java.sql.SQLException;
import java.util.List;

public interface IAddressService {

    public abstract Address getAddressById(Integer id, User user);

    public abstract List<Address> getAllAddressByUser(User user);

    public abstract Integer insertAddress(Address address, User user) throws SQLException;

    public abstract boolean updateAddress(Integer id, Address updatedAddress, User user) throws SQLException;

    public abstract boolean deleteAddress(Integer id, User user);

}
