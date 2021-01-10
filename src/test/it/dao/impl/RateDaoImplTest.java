package dao.impl;

import connection.H2Connection;
import dao.IRateDao;
import entities.Rate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

public class RateDaoImplTest {

    private IRateDao rateDao;

    @Before
    public void initialize() {
        this.rateDao = new RateDaoImpl(H2Connection.getH2DatabaseConnection());
    }

    @Test
    public void test_getRateById_Should_Return_Rate_Id_2() {
        Rate expectedRate2 = new Rate(2, 1, 1);

        Rate rate = rateDao.getRateById(2);

       assertThat(expectedRate2, samePropertyValuesAs(rate));
    }

    @Test
    public void test_getRateById_Should_Return_Null_For_Nonexistent_Rate_Id() {
        int nonExistentRateId = 37;

        Rate rate = rateDao.getRateById(nonExistentRateId);

        Assert.assertNull(rate);
    }

    @Test
    public void test_insertRate_Should_Return_Rate_Id_On_Successful_Insert() {
        Rate rate = new Rate(null, 1, 1);
        Integer rateId = rateDao.insert(rate);

        Assert.assertEquals(Integer.valueOf(11), rateId);
    }

    @Test
    public void test_upvote_Should_Return_True_On_Successful_Upvote() {
        Rate rate = rateDao.getRateById(9);

        Assert.assertEquals(Integer.valueOf(9), rate.getRateId());
        Assert.assertEquals(Integer.valueOf(0), Integer.valueOf(rate.getUpvotes()));
        Assert.assertEquals(Integer.valueOf(0), Integer.valueOf(rate.getDownvotes()));

        boolean upvoted = rateDao.update(rate);
        Assert.assertTrue(upvoted);
    }

    @Test
    public void test_upvote_Should_Return_False_For_Nonexistent_Rate_Id() {
        Integer nonExistentRateId = 43;
        Rate rate = new Rate(nonExistentRateId, 1, 1);

        boolean upvoted = rateDao.update(rate);

        Assert.assertFalse(upvoted);
    }

    @Test
    public void test_downvote_Should_Return_True_On_Successful_Downvote() {
        Rate rate = rateDao.getRateById(9);

        Assert.assertEquals(Integer.valueOf(9), rate.getRateId());
        Assert.assertEquals(Integer.valueOf(0), Integer.valueOf(rate.getUpvotes()));
        Assert.assertEquals(Integer.valueOf(0), Integer.valueOf(rate.getDownvotes()));

        boolean downvoted = rateDao.update(rate);
        Assert.assertTrue(downvoted);
    }

    @Test
    public void test_downvote_Should_Return_False_For_Nonexistent_Rate_Id() {
        Integer nonExistentRateId = 78;
        Rate rate = new Rate(nonExistentRateId, 1, 4);

        boolean downvoted = rateDao.update(rate);

        Assert.assertFalse(downvoted);
    }

    @Test
    public void test_deleteRateById_Should_Return_True_On_Successful_Delete() {
        Rate rate = new Rate(null ,10, 4);
        Integer rateId = rateDao.insert(rate);

        Assert.assertEquals(Integer.valueOf(11), rateId);

        rate = rateDao.getRateById(rateId);
        Assert.assertNotNull(rate);

        boolean deleted = rateDao.deleteRateById(11);
        Assert.assertTrue(deleted);

        rate = rateDao.getRateById(11);
        Assert.assertNull(rate);
    }

    @Test
    public void test_deleteRateById_Should_Return_False_For_Nonexistent_Rate_Id() {
        int nonExistentRateId = 100;

        boolean deleted = rateDao.deleteRateById(nonExistentRateId);

        Assert.assertFalse(deleted);
    }


}
