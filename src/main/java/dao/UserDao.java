package dao;

import model.User;

public interface UserDao {

    User findByName(String name);
    void setName(User user, String newName);
    void setPassword(User user, String newPassword);
    void createUser(User user);
    void deleteUser(User user);


// void setUserName(User user, String name);
}
