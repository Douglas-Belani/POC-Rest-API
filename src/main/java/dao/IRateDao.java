package dao;


import entities.Rate;

public interface IRateDao {

    public abstract Rate getRateById(int id);

    public abstract Integer insert(Rate rate);

    public abstract boolean update(Rate rate);

    public abstract boolean deleteRateById(int id);
}
