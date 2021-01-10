package dao.impl;

import connection.H2Connection;
import dao.IAuxRateUserDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

public class AuxRateUserDaoTest {

    private IAuxRateUserDao auxRateUserDao;

    @Before
    public void initialize() {
        this.auxRateUserDao = new AuxRateUserDao(H2Connection.getH2DatabaseConnection());
    }

    @Test
    public void test_isAlreadyVoted_Should_Return_1_For_User_Id_1_Rate_Id_2() throws SQLException {
        int userId = 1;
        int rateId = 2;

        int isAlreadyVotedCode = auxRateUserDao.isAlreadyVoted(userId, rateId);
        Assert.assertEquals(1, isAlreadyVotedCode);
    }

    @Test
    public void test_isAlreadyVoted_Should_Return_0_For_User_Id_1_Rate_Id_1() throws SQLException {
        int userId = 1;
        int rateId = 1;

        int isAlreadyVotedCode = auxRateUserDao.isAlreadyVoted(userId, rateId);

        Assert.assertEquals(0, isAlreadyVotedCode);
    }

    @Test
    public void test_isAlreadyVoted_Should_Return_0_For_Nonexistent_User_Id() throws SQLException {
        int nonExistentUserId = 50;
        int rateId = 4;

        int isAlreadyVotedCode = auxRateUserDao.isAlreadyVoted(nonExistentUserId, rateId);
        Assert.assertEquals(0, isAlreadyVotedCode);
    }

    @Test
    public void test_isAlreadyVoted_Should_Return_0_For_Nonexistent_Rate_Id() throws SQLException {
        int userId = 2;
        int nonexistentRateId = 123;

        int isAlreadyVoted = auxRateUserDao.isAlreadyVoted(userId, nonexistentRateId);
        Assert.assertEquals(0, isAlreadyVoted);
    }

    @Test
    public void test_insertUpvote_Should_Return_True_On_Successful_Insert() {
        int userId = 3;
        int rateId = 2;

        boolean inserted = auxRateUserDao.insertUpvote(userId, rateId);
        Assert.assertTrue(inserted);
    }

    @Test
    public void test_insertUpvote_Should_Return_False_If_isAlreadyVoted() throws SQLException {
        int userId = 1;
        int rateId = 2;

        int isAlreadyVoted = auxRateUserDao.isAlreadyVoted(1, 2);
        Assert.assertEquals(1, isAlreadyVoted);

        boolean upvoted = auxRateUserDao.insertUpvote(userId, rateId);
        Assert.assertFalse(upvoted);
    }

    @Test
    public void test_insertDownvote_Should_Return_True_On_Successful_Insert() {
        int userId = 2;
        int rateId = 1;

        boolean downvoted = auxRateUserDao.insertDownvote(userId, rateId);
        Assert.assertTrue(downvoted);
    }

    @Test
    public void test_insertDownvote_Should_Return_False_If_isAlreadyVoted() throws SQLException {
        int userId = 1;
        int rateId = 3;

        int isAlreadyUpvoted = auxRateUserDao.isAlreadyVoted(userId, rateId);
        Assert.assertEquals(1, isAlreadyUpvoted);

        boolean downvoted = auxRateUserDao.insertDownvote(userId, rateId);
        Assert.assertFalse(downvoted);
    }

    @Test
    public void test_delete_Should_Return_True_On_Successful_Delete() {
        int userId = 1;
        int rateId = 7;

        boolean deleted = auxRateUserDao.delete(userId, rateId);
        Assert.assertTrue(deleted);
    }

    @Test
    public void test_delete_Should_Return_False_For_Nonexistent_User_Id() {
        int nonexistentUserId = 53;
        int rateId = 8;

        boolean deleted = auxRateUserDao.delete(nonexistentUserId, rateId);
        Assert.assertFalse(deleted);
    }

    @Test
    public void test_delete_Should_Return_False_For_Nonexistent_Rate_Id() {
        int userId = 3;
        int nonexistentRateId = 99;

        boolean deleted = auxRateUserDao.delete(userId, nonexistentRateId);
        Assert.assertFalse(deleted);
    }

    @Test
    public void test_deleteByRateId_Should_Delete_All_Entries_With_Same_Rate_Id_And_Return_True() throws SQLException {
        int rateId = 7;
        boolean deleted = auxRateUserDao.deleteByRateId(7);

        int userId1 = 1;
        int userId2 = 2;

        int isAlreadyVoted1 = auxRateUserDao.isAlreadyVoted(userId1, rateId);
        int isAlreadyVoted2 = auxRateUserDao.isAlreadyVoted(userId2, rateId);
        Assert.assertTrue(deleted);
        Assert.assertEquals(0, isAlreadyVoted1);
        Assert.assertEquals(0, isAlreadyVoted2);
    }

    @Test
    public void test_deleteByRateId_Should_Return_False_For_Nonexistent_RateId() {
        int nonexistentRateId = 533;

        boolean deleted = auxRateUserDao.deleteByRateId(nonexistentRateId);
        Assert.assertFalse(deleted);
    }
}
