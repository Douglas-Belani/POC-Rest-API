package dao;

import entities.City;

public interface ICityDao {

    public abstract City getCityByNameAndStateInitials(String name, String initials);

    public abstract Integer insertCity(City city);

}
