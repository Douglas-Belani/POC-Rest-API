package services;

import entities.Rate;

import java.sql.SQLException;

public interface IRateService {

    public abstract Rate getRateById(Integer id);

    public abstract boolean upvote(Integer userId, Integer rateId) throws SQLException;

    public abstract boolean downvote(Integer userId, Integer rateId) throws SQLException;

    public abstract boolean deleteById(Integer id);

    public abstract Rate createRate();

}
