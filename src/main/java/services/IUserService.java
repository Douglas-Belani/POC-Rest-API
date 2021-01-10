package services;

import entities.User;

import java.time.LocalDate;

public interface IUserService {

    public abstract User getUserById(Integer id);

    public abstract User getUserByEmail(String email);

    public abstract Integer createUser(User user);

    public abstract boolean updateUser(User user, Integer id, User updatedUser);

    public abstract boolean deleteUserById(User user, Integer id);

}
