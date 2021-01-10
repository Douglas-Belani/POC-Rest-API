package dao.impl;

import connection.H2Connection;
import dao.IUserDao;
import entities.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class UserDaoImplTest {

    private static IUserDao userDao;

    @Before
    public void initialize() {
        userDao = new UserDaoImpl(H2Connection.getH2DatabaseConnection());
    }

    @Test
    public void test_getUserById_Should_Return_User_Id_2() {
        User expectedUser2 = new User(2, "user2", "222222222-22",
                "user2@gmail.com", "user2Password",
                LocalDate.of(2002, 10, 10));

        User user = userDao.getUserById(2);

        Assert.assertEquals(expectedUser2.getUserId(), user.getUserId());
        Assert.assertEquals(expectedUser2.getFullName(), user.getFullName());
        Assert.assertEquals(expectedUser2.getCpf(), user.getCpf());
        Assert.assertEquals(expectedUser2.getEmail(), user.getEmail());
        Assert.assertEquals(expectedUser2.getPassword(), user.getPassword());
        Assert.assertEquals(expectedUser2.getBirthDate(), user.getBirthDate());
        Assert.assertEquals(expectedUser2.getListedProducts(), user.getListedProducts());
        Assert.assertEquals(expectedUser2.getUpvotedProducts(), user.getUpvotedProducts());
        Assert.assertEquals(expectedUser2.getOrders(), user.getOrders());

    }

    @Test
    public void test_getUserById_Should_Return_Null_For_Nonexistent_User_Id() {
        int nonExistentUserId = 10;

        User user = userDao.getUserById(nonExistentUserId);

        Assert.assertNull(user);
    }

    @Test
    public void test_getUserByEmail_Should_Return_User_Id_1_For_Email_user1() {
        User expectedUser1 = new User(1, "user1", "111111111-11",
                "user1@gmail.com", "user1Password",
                LocalDate.of(2005, 8, 10));

        User user = userDao.getUserByEmail("user1@gmail.com");

        Assert.assertEquals(expectedUser1.getUserId(), user.getUserId());
        Assert.assertEquals(expectedUser1.getFullName(), user.getFullName());
        Assert.assertEquals(expectedUser1.getCpf(), user.getCpf());
        Assert.assertEquals(expectedUser1.getEmail(), user.getEmail());
        Assert.assertEquals(expectedUser1.getPassword(), user.getPassword());
        Assert.assertEquals(expectedUser1.getBirthDate(), user.getBirthDate());
        Assert.assertEquals(expectedUser1.getListedProducts(), user.getListedProducts());
        Assert.assertEquals(expectedUser1.getUpvotedProducts(), user.getUpvotedProducts());
        Assert.assertEquals(expectedUser1.getOrders(), user.getOrders());
    }

    @Test
    public void test_getUserByEmail_Should_Return_Null_For_Nonexistent_Email() {
        String nonExistentEmail = "nonExistentEmail@gmail.com";

        User user = userDao.getUserByEmail(nonExistentEmail);

        Assert.assertNull(user);
    }

    @Test
    public void test_insertUser_Should_Return_True_On_Successful_Insert() {
        User newUser = new User(null, "user5", "555555555-55",
                "user5@gmail.com", "user5Password",
                LocalDate.of(2020, 9, 30 ));

        Integer userId = userDao.insertUser(newUser);
        newUser.setUserId(userId);

        User user = userDao.getUserById(userId);

        Assert.assertEquals(newUser.getUserId(), user.getUserId());
        Assert.assertEquals(newUser.getFullName(), user.getFullName());
        Assert.assertEquals(newUser.getCpf(), user.getCpf());
        Assert.assertEquals(newUser.getEmail(), user.getEmail());
        Assert.assertEquals(newUser.getPassword(), user.getPassword());
        Assert.assertEquals(newUser.getBirthDate(), user.getBirthDate());
        Assert.assertEquals(newUser.getListedProducts(), user.getListedProducts());
        Assert.assertEquals(newUser.getUpvotedProducts(), user.getUpvotedProducts());
        Assert.assertEquals(newUser.getOrders(), user.getOrders());
    }

    @Test
    public void test_updateUser_Should_Return_True_On_Successful_Update() {
        User oldUser = userDao.getUserById(1);
        oldUser.setFullName("userUpdate");
        oldUser.setEmail("user1Update@gmail.com");

        boolean updated = userDao.updateUser(oldUser, 1);

        Assert.assertTrue(updated);

        User newUser = userDao.getUserById(1);

        Assert.assertEquals(oldUser.getUserId(), newUser.getUserId());
        Assert.assertEquals(oldUser.getFullName(), newUser.getFullName());
        Assert.assertEquals(oldUser.getCpf(), newUser.getCpf());
        Assert.assertEquals(oldUser.getEmail(), newUser.getEmail());
        Assert.assertEquals(oldUser.getPassword(), newUser.getPassword());
        Assert.assertEquals(oldUser.getBirthDate(), newUser.getBirthDate());
        Assert.assertEquals(oldUser.getListedProducts(), newUser.getListedProducts());
        Assert.assertEquals(oldUser.getUpvotedProducts(), newUser.getUpvotedProducts());
        Assert.assertEquals(oldUser.getOrders(), newUser.getOrders());

    }

    @Test
    public void test_updateUser_Should_Return_False_For_Nonexistent_User_Id() {
        int nonExistentUserId = 10;
        User newUser = new User(nonExistentUserId, "user1", "111111111-11",
                "user1@gmail.com", "user1Password",
                LocalDate.of(2005, 8, 10));

        boolean updated = userDao.updateUser(newUser, nonExistentUserId);

        Assert.assertFalse(updated);
    }

    @Test
    public void test_DeleteById_Should_Return_True_On_Successful_Delete() {
        int userId = 4;

        boolean deleted = userDao.deleteUser(userId);

        Assert.assertTrue(deleted);

        User user = userDao.getUserById(4);

        Assert.assertNull(user);
    }

    @Test
    public void test_DeleteById_Should_Return_False_For_Nonexistent_User_Id() {
        int nonExistentUserId = 10;

        boolean deleted = userDao.deleteUser(nonExistentUserId);
        Assert.assertFalse(deleted);
    }
}
