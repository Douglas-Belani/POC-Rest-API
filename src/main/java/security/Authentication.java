package security;

import dao.IUserDao;
import entities.User;
import util.PasswordUtil;

public class Authentication {

    private IUserDao userDao;

    public Authentication(IUserDao userDao) {
        this.userDao = userDao;
    }

    public boolean authenticateUser(String email, String password) {
        User user = userDao.getUserByEmail(email);
        if (user == null) {
            return false;
        }

        return PasswordUtil.checkPassword(password, user.getPassword());
    }

}
