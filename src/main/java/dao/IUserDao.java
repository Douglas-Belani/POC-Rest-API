package dao;

import entities.User;

public interface IUserDao {

    public abstract User getUserById(int id);

    public abstract User getUserByEmail(String email);

    public abstract Integer insertUser(User user);

    public abstract boolean updateUser(User user, int id);

    public abstract boolean deleteUser(int id);

}
