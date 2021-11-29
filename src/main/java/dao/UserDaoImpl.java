package dao;

import lombok.SneakyThrows;
import model.User;

import java.sql.*;

public class UserDaoImpl implements UserDao {

    private final String DB_URL = "jdbc:MySQL://localhost:3306/my_schema?serverTimezone=UTC";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "root";

    @SneakyThrows
    @Override
    public User findByName(String name) {
        ResultSet resultSet = getStatement().executeQuery("SELECT login, password from USER");
        while (resultSet.next()) {
            if (name.equalsIgnoreCase(resultSet.getString("login"))) {
                return new User(name, resultSet.getString("password"));
            }
        }
        return null;
    }

    @SneakyThrows
    @Override
    public void setName(User user, String newName) {
        if (user.getPassword().equals(findByName(user.getName()).getPassword())) {
            getStatement().executeUpdate("update user set login = '" + newName + "' where login = '" + user.getName() + "';");
            System.out.println("Login changed");
        }
    }

    @SneakyThrows
    @Override
    public void setPassword(User user, String newPassword) {
        if (user.getPassword().equals(findByName(user.getName()).getPassword())) {
            getStatement().executeUpdate("update user set password = '" + newPassword + "' where password = '" + user.getPassword() + "';");
            System.out.println("Password changed");
        }
    }

    @SneakyThrows
    @Override
    public void createUser(User user) {
        if (findByName(user.getName()) == null) {
            getStatement().executeUpdate("insert into user (login, password) values ('" + user.getName() + "','" + user.getPassword() + "');");
            System.out.println("User add into database ");
        }
    }

    @SneakyThrows
    @Override
    public void deleteUser(User user) {
        if (user.getPassword().equals(findByName(user.getName()).getPassword())) {
            getStatement().executeUpdate("delete from user where login = '" + user.getName() + "';");
            System.out.println("User delete from database");
        }
    }

    private Statement getStatement() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        return connection.createStatement();
    }

}