package services.impl;

import dao.IRateDao;
import dao.impl.AuxRateUserDao;
import entities.Rate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import resources.exception.ResourceNotFoundException;

import java.sql.Connection;
import java.sql.SQLException;

@RunWith(MockitoJUnitRunner.class)
public class RateServiceTest {

    @Mock
    private Connection conn;

    @Mock
    private AuxRateUserDao auxRateUserDao;

    @Mock
    private IRateDao rateDao;


    private RateServiceImpl rateService;

    @Before
    public void initialize() {
        rateService = new RateServiceImpl(conn, rateDao, auxRateUserDao);
    }

    @Test
    public void test_upvote_Should_Return_True_For_Not_Already_Upvoted_Rate_By_User()
            throws SQLException {
        int userId = 1;
        int rateId = 3;

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.doNothing().when(conn).commit();
        Mockito.when(auxRateUserDao.isAlreadyVoted(userId, rateId)).thenReturn(0);
        Rate rate = new Rate(3, 0, 1);
        Mockito.when(rateDao.getRateById(rateId)).thenReturn(rate);
        Mockito.when(rateDao.update(rate)).thenReturn(true);
        Mockito.when(auxRateUserDao.insertUpvote(userId, rateId)).thenReturn(true);
        Mockito.doNothing().when(conn).setAutoCommit(true);
        Assert.assertTrue(rateService.upvote(userId, rateId));
        Assert.assertEquals(1, rate.getUpvotes());
    }

    @Test
    public void test_upvote_Should_Return_False_For_Nonexistent_User_Id() throws SQLException {
        int invalidUserId = 55;
        int rateId = 2;

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(auxRateUserDao.isAlreadyVoted(invalidUserId, rateId)).thenReturn(0);
        Rate rate = new Rate(2, 1, 1);
        Mockito.when(rateDao.getRateById(rateId)).thenReturn(rate);
        Mockito.when(rateDao.update(rate)).thenReturn(true);
        Mockito.when(auxRateUserDao.insertUpvote(invalidUserId, rateId)).thenReturn(false);
        Mockito.doNothing().when(conn).rollback();
        Assert.assertFalse(rateService.upvote(invalidUserId, rateId));
        Mockito.verify(conn, Mockito.times(1)).rollback();
    }

    @Test
    public void test_upvote_Should_Return_True_For_Already_Upvoted_Product_And_Decrement_Rate_Upvotes() throws SQLException {
        int userId = 1;
        int rateId = 2;

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(auxRateUserDao.isAlreadyVoted(userId, rateId)).thenReturn(1);
        Rate rate = new Rate(2, 1, 1);
        Mockito.when(rateDao.getRateById(rateId)).thenReturn(rate);
        Mockito.when(rateDao.update(rate)).thenReturn(true);
        Mockito.when(auxRateUserDao.delete(userId, rateId)).thenReturn(true);
        Mockito.doNothing().when(conn).commit();
        Assert.assertTrue(rateService.upvote(userId, rateId));
        Assert.assertEquals(0, rate.getUpvotes());
    }

    @Test
    public void test_upvote_Should_Return_False_If_RateDao_Upvote_Returns_False() throws SQLException {
        int rateId = 1;
        int userId = 1;
        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(auxRateUserDao.isAlreadyVoted(userId, rateId)).thenReturn(1);
        Rate rate = new Rate(1, 2, 0);
        Mockito.when(rateDao.getRateById(rateId)).thenReturn(rate);
        Mockito.when(rateDao.update(rate)).thenReturn(false);
        Mockito.when(auxRateUserDao.delete(userId, rateId)).thenReturn(true);
        Mockito.doNothing().when(conn).rollback();
        Assert.assertFalse(rateService.upvote(userId, rateId));
        Mockito.verify(conn, Mockito.times(1)).rollback();
    }

    @Test
    public void test_upvote_Should_Return_False_If_AuxRateUserDao_Returns_False() throws SQLException {
        int rateId = 1;
        int userId = 1;
        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(auxRateUserDao.isAlreadyVoted(userId, rateId)).thenReturn(1);
        Rate rate = new Rate(1, 2, 0);
        Mockito.when(rateDao.getRateById(rateId)).thenReturn(rate);
        Mockito.when(rateDao.update(rate)).thenReturn(true);
        Mockito.when(auxRateUserDao.delete(userId, rateId)).thenReturn(false);
        Mockito.doNothing().when(conn).rollback();
        Assert.assertFalse(rateService.upvote(userId, rateId));
        Mockito.verify(conn, Mockito.times(1)).rollback();
    }

    @Test
    public void test_upvote_Should_Throw_ResourceNotFoundException_For_Nonexistent_Rate_Id() throws SQLException {
        int userId = 1;
        int invalidRateId = 35;

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(auxRateUserDao.isAlreadyVoted(userId, invalidRateId)).thenReturn(0);
        Mockito.when(rateDao.getRateById(invalidRateId)).thenReturn(null);
        Mockito.doNothing().when(conn).setAutoCommit(true);
        ResourceNotFoundException thrown = Assert.assertThrows(ResourceNotFoundException.class, () ->
                rateService.upvote(userId, invalidRateId));
        Assert.assertEquals("Rate id " + invalidRateId + " not found", thrown.getMessage());
    }

    @Test
    public void test_downvote_Should_Return_True_For_Not_Already_Downvoted_Products() throws SQLException{
        int userId = 1;
        int rateId = 3;

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(auxRateUserDao.isAlreadyVoted(userId, rateId)).thenReturn(0);
        Rate rate = new Rate(1, 2, 0);
        Mockito.when(rateDao.getRateById(rateId)).thenReturn(rate);
        Mockito.when(rateDao.update(rate)).thenReturn(true);
        Mockito.when(auxRateUserDao.insertDownvote(userId, rateId)).thenReturn(true);
        Mockito.doNothing().when(conn).commit();
        Assert.assertTrue(rateService.downvote(userId, rateId));
        Assert.assertEquals(1, rate.getDownvotes());
        Mockito.verify(conn, Mockito.times(1)).commit();
    }

    @Test
    public void test_downvote_Should_Throw_ResourceNotFoundException_For_Nonexistent_Rate_Id() throws SQLException{
        int userId = 1;
        int nonExistentRateId = 85;

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(auxRateUserDao.isAlreadyVoted(userId, nonExistentRateId)).thenReturn(0);
        Mockito.when(rateDao.getRateById(nonExistentRateId)).thenReturn(null);
        Mockito.doNothing().when(conn).setAutoCommit(true);
        ResourceNotFoundException thrown = Assert.assertThrows(ResourceNotFoundException.class, () ->
                rateService.downvote(userId, nonExistentRateId));
        Assert.assertEquals("Rate id " + nonExistentRateId + " not found", thrown.getMessage());
    }

    @Test
    public void test_downvote_Should_Return_False_For_Nonexistent_User_Id() throws SQLException {
        int nonExistentUserId = 89;
        int rateId = 2;

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(auxRateUserDao.isAlreadyVoted(nonExistentUserId, rateId)).thenReturn(0);
        Rate rate = new Rate(2, 0, 1);
        Mockito.when(rateDao.getRateById(rateId)).thenReturn(rate);
        Mockito.when(rateDao.update(rate)).thenReturn(true);
        Mockito.when(auxRateUserDao.isAlreadyVoted(nonExistentUserId, rateId))
                .thenReturn(0);
        Mockito.doNothing().when(conn).rollback();
        Assert.assertFalse(rateService.downvote(nonExistentUserId, rateId));
        Mockito.verify(conn, Mockito.times(1)).rollback();
    }

    @Test
    public void test_downvote_Should_Return_True_For_Not_Already_Downvoted() throws SQLException {
        int userId = 2;
        int rateId = 2;

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(auxRateUserDao.isAlreadyVoted(userId, rateId)).thenReturn(-1);
        Rate rate = new Rate(2, 1, 1);
        Mockito.when(rateDao.getRateById(rateId)).thenReturn(rate);
        Mockito.when(rateDao.update(rate)).thenReturn(true);
        Mockito.when(auxRateUserDao.delete(userId, rateId)).thenReturn(true);
        Mockito.doNothing().when(conn).commit();
        Assert.assertTrue(rateService.downvote(userId, rateId));
        Assert.assertEquals(0, rate.getDownvotes());
        Mockito.verify(conn, Mockito.times(1)).commit();
    }

    @Test
    public void test_downvote_Should_Return_False_If_RateDao_Update_Returns_False() throws SQLException{
        int userId = 2;
        int rateId = 2;

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(auxRateUserDao.isAlreadyVoted(userId, rateId)).thenReturn(-1);
        Rate rate = new Rate(2, 1, 1);
        Mockito.when(rateDao.getRateById(rateId)).thenReturn(rate);
        Mockito.when(rateDao.update(rate)).thenReturn(false);
        Mockito.when(auxRateUserDao.delete(userId, rateId)).thenReturn(true);
        Mockito.doNothing().when(conn).rollback();
        Assert.assertFalse(rateService.downvote(userId, rateId));
        Mockito.verify(conn, Mockito.times(1)).rollback();
    }

    @Test
    public void test_downvote_Should_Return_False_If_AuxRateUser_Delete_Returns_False() throws SQLException{
        int userId = 2;
        int rateId = 2;

        Mockito.doNothing().when(conn).setAutoCommit(false);
        Mockito.when(auxRateUserDao.isAlreadyVoted(userId, rateId)).thenReturn(-1);
        Rate rate = new Rate(2, 1, 1);
        Mockito.when(rateDao.getRateById(rateId)).thenReturn(rate);
        Mockito.when(rateDao.update(rate)).thenReturn(true);
        Mockito.when(auxRateUserDao.delete(userId, rateId)).thenReturn(false);
        Mockito.doNothing().when(conn).rollback();
        Mockito.doNothing().when(conn).setAutoCommit(true);
        Assert.assertFalse(rateService.downvote(userId, rateId));
        Mockito.verify(conn, Mockito.times(1)).rollback();
    }
}
