package services.impl;

import dao.IAddressDao;
import dao.ICityDao;
import dao.IStateDao;
import dao.impl.AddressDaoImpl;
import dao.impl.CityDaoImpl;
import dao.impl.StateDaoImpl;
import entities.Address;
import entities.City;
import entities.State;
import entities.User;
import resources.exception.ResourceNotFoundException;
import services.IAddressService;
import services.exceptions.UnauthorizedException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AddressServiceImpl implements IAddressService {

    private Connection conn;
    private IAddressDao addressDao;
    private ICityDao cityDao;
    private IStateDao stateDao;

    public AddressServiceImpl(Connection conn) {
        this(new AddressDaoImpl(conn), new CityDaoImpl(conn), new StateDaoImpl(conn));
        this.conn = conn;
    }

    public AddressServiceImpl(IAddressDao addressDao, ICityDao cityDao, IStateDao stateDao) {
        this.addressDao = addressDao;
        this.cityDao = cityDao;
        this.stateDao = stateDao;
    }

    public AddressServiceImpl(Connection conn, IAddressDao addressDao, ICityDao cityDao, IStateDao stateDao) {
        this.conn = conn;
        this.addressDao = addressDao;
        this.cityDao = cityDao;
        this.stateDao = stateDao;
    }

    @Override
    public Address getAddressById(Integer id, User user) {
        List<Address> addresses = getAllAddressByUser(user);
        Address address = addressDao.getAddressById(id);

        if (address == null) {
            throw new ResourceNotFoundException("Address id " + id + " not found.");
        }

        if (!addresses.contains(address)) {
            throw new UnauthorizedException("Can't access another user's information.");
        }

        return address;
    }

    @Override
    public List<Address> getAllAddressByUser(User user) {
        return addressDao.getAllAddressesByUserId(user.getUserId());
    }

    @Override
    public Integer insertAddress(Address address, User user) throws SQLException {
        try {
            Integer addressId;

            conn.setAutoCommit(false);

            State state = stateDao.getStateByInitials(address.getCity().getState().getInitials());
            City city = cityDao.getCityByNameAndStateInitials
                    (address.getCity().getName(), address.getCity().getState().getInitials());

            if (state == null) {
                Integer stateId = stateDao.insertState(address.getCity().getState());
                if (stateId == null) {
                    conn.rollback();
                    return null;
                }
                address.getCity().getState().setStateId(stateId);

                Integer cityId = cityDao.insertCity(address.getCity());
                if (cityId == null) {
                    conn.rollback();
                    return null;
                }
                address.getCity().setCityId(cityId);

            } else if (city == null) {
                address.getCity().getState().setStateId(state.getStateId());

                Integer cityId = cityDao.insertCity(address.getCity());
                if (cityId == null) {
                    conn.rollback();
                    return null;
                }
                address.getCity().setCityId(cityId);

            } else {
                address.getCity().getState().setStateId(state.getStateId());
                address.getCity().setCityId(city.getCityId());
            }

            addressId = addressDao.insertAddress(address, user.getUserId());

            if (addressId == null) {
                conn.rollback();
                return null;
            }

            conn.commit();
            return addressId;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    @Override
    public boolean updateAddress(Integer id, Address updatedAddress, User user) throws SQLException {
        try {
            conn.setAutoCommit(false);

            Address persistedAddress = getAddressById(id, user);

            updatedAddress.setAddressId(persistedAddress.getAddressId());

            State updatedState = stateDao.getStateByInitials(
                    updatedAddress.getCity().getState().getInitials());

            City updatedCity = cityDao.getCityByNameAndStateInitials(
                    updatedAddress.getCity().getName(),
                    updatedAddress.getCity().getState().getInitials());

            if (updatedState == null) {
                State state = updatedAddress.getCity().getState();
                Integer stateId = stateDao.insertState(state);
                state.setStateId(stateId);

                City city = updatedAddress.getCity();
                Integer cityId = cityDao.insertCity(city);
                city.setCityId(cityId);

            } else if (updatedCity == null) {
                updatedAddress.getCity().getState().setStateId(updatedState.getStateId());

                Integer cityId = cityDao.insertCity(updatedAddress.getCity());
                updatedAddress.getCity().setCityId(cityId);

            } else {
                updatedAddress.getCity().getState().setStateId(updatedState.getStateId());
                updatedAddress.getCity().setCityId(updatedCity.getCityId());
            }

            boolean updated = addressDao.updateAddress(updatedAddress);

            if (updated) {
                conn.commit();
                return true;

            } else {
                conn.rollback();
                return false;
            }

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    @Override
    public boolean deleteAddress(Integer id, User user) {
        getAddressById(id, user);
        return addressDao.deleteAddressById(id);
    }
}
