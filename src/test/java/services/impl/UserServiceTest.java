package services.impl;

import dao.IUserDao;
import entities.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import services.IEmailService;
import services.IUserService;
import services.exceptions.UnauthorizedException;

import java.sql.Connection;
import java.time.LocalDate;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserServiceTest {

    @Mock
    private IUserDao userDao;

    @Mock
    private IEmailService email;

    @Mock
    private Connection conn;

    private IUserService userService;

    @Before
    public void initialize() {
        userService = new UserServiceImpl(conn, userDao, email);
    }

    @Test
    public void test_getUserByEmail_Should_Throw_IllegalArgumentException_When_Null_String_Is_Passed() {
        String email = null;

        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class,
                () -> userService.getUserByEmail(email));
        Assert.assertEquals("Email must not be empty.", thrown.getMessage());
    }

    @Test
    public void test_getUserByEmail_Should_Throw_IllegalArgumentException_When_Empty_String_Is_Passed() {
        String email = "";

        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class,
                () -> userService.getUserByEmail(email));
        Assert.assertEquals("Email must not be empty.", thrown.getMessage());
    }

    @Test
    public void test_getUserByEmail_Should_Throw_IllegalArgumentException_For_Nonexistent_Email() {
        String nonExistentEmail = "nonExistentEmail";

        Mockito.when(userDao.getUserByEmail(nonExistentEmail)).thenReturn(null);

        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class,
                () -> userService.getUserByEmail(nonExistentEmail));
        Assert.assertEquals("Wrong email.", thrown.getMessage());
    }

    @Test
    public void test_getUserByEmail_Should_Return_User_Id_1_For_Email_email_gmail_com() {
        String email = "email@gmail.com";
        User user = new User(1, "fullName", "111111111-11", email,
                "password", LocalDate.now());
        Mockito.when(userDao.getUserByEmail(email)).thenReturn(user);

        User expectedUser = new User(1, "fullName", "111111111-11",
                "email@gmail.com", "password", LocalDate.now());
        User retrievedUser = userService.getUserByEmail(email);
        Assert.assertEquals(expectedUser.getUserId(), retrievedUser.getUserId());
        Assert.assertEquals(expectedUser.getFullName(), retrievedUser.getFullName());
        Assert.assertEquals(expectedUser.getCpf(), retrievedUser.getCpf());
        Assert.assertEquals(expectedUser.getEmail(), retrievedUser.getEmail());
        Assert.assertEquals(expectedUser.getPassword(), retrievedUser.getPassword());
        Assert.assertEquals(expectedUser.getBirthDate(), retrievedUser.getBirthDate());
    }

    @Test
    public void test_update_User_Should_Return_True_On_Successful_Update() {
        int id = 1;
        String newFullName = "newFullName";
        String newCpf = "222222222-22";
        String newEmail = "newEmail@gmail.com";
        String newPassword = "newPassword";
        LocalDate newBirthDate = LocalDate.now().minusWeeks(10);
        User updatedUser = new User(id, newFullName, newCpf,newEmail, newPassword, newBirthDate);

        User user = new User(id, "fullName", "111111111-11",
                "email@gmail.com", "password", LocalDate.now());

        Mockito.when(userDao.getUserById(id)).thenReturn(user);
        Mockito.when(userDao.updateUser(updatedUser, id)).thenReturn(true);

        Assert.assertTrue(userService.updateUser(user, id, updatedUser));
    }

    @Test
    public void test_deleteUserById_Should_Return_True_On_Success_Delete() {
        int id = 1;

        User user = new User(id, "fullName", "111111111-11",
                "email", "password", LocalDate.now());

        Mockito.when(userDao.getUserById(id)).thenReturn(user);
        Mockito.when(userDao.deleteUser(id)).thenReturn(true);

        Assert.assertTrue(userService.deleteUserById(user, id));
    }
}
