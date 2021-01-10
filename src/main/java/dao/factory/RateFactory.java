package dao.factory;

import entities.Rate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RateFactory {

    public static Rate getRateFromResultSetRs(ResultSet rs) throws SQLException {
        return new Rate(rs.getInt("rateId"), rs.getInt("upvotes"),
                rs.getInt("downvotes"));
    }
}
