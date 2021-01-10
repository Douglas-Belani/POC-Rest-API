package dao;

import entities.Address;

import java.util.List;

public interface IAddressDao {

    public abstract Address getAddressById(int id);

    public abstract List<Address> getAllAddressesByUserId(int userId);

    public abstract Integer insertAddress(Address address, int userId);

    public abstract boolean updateAddress(Address address);

    public abstract boolean deleteAddressById(int addressId);

}
