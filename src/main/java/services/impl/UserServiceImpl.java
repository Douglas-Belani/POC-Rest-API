package services.impl;

import dao.IUserDao;
import dao.impl.UserDaoImpl;
import entities.User;
import resources.exception.ResourceNotFoundException;
import services.IEmailService;
import services.IUserService;

import java.sql.Connection;

public class UserServiceImpl implements IUserService {

    private Connection conn;
    private IUserDao userDao;
    private IEmailService emailService;

    public UserServiceImpl(Connection conn, IEmailService emailService) {
        this.conn = conn;
        this.userDao = new UserDaoImpl(conn);
        this.emailService = emailService;
    }

    public UserServiceImpl(Connection conn, IUserDao userDao, IEmailService emailService) {
        this.conn = conn;
        this.userDao = userDao;
        this.emailService = emailService;
    }


    @Override
    public User getUserById(Integer id) {
        User user = userDao.getUserById(id);

        if (user == null) {
            throw new ResourceNotFoundException("User id " + id + " not found");
        }

        return userDao.getUserById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be empty.");
        }

        User user = userDao.getUserByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("Wrong email.");
        }

         return user;
    }

    @Override
    public Integer createUser(User user) {
        Integer id = userDao.insertUser(user);
        user.setUserId(id);

        Thread thread = new Thread(() -> {
            if (id != null) {
                emailService.sendEmail(user.getEmail(), "Account creation", "Your " +
                    "account was successfully created! " + user.toString());
        }});
        thread.start();
        return id;
    }

    @Override
    public boolean updateUser(User user, Integer id, User updatedUser) {
        updatedUser.setUserId(user.getUserId());
        return userDao.updateUser(updatedUser, id);
    }

    @Override
    public boolean deleteUserById(User user, Integer id) {
        return userDao.deleteUser(id);
    }
}
