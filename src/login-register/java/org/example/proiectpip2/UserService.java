package org.example.proiectpip2;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    public static List<User> users = new ArrayList<User>();

    public static void addUser(User user) {
        users.add(user);
    }
    public static User findUser(String username,String password) {
        for (User user : users) {
            if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}
