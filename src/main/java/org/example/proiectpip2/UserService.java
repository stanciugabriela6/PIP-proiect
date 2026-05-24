package org.example.proiectpip2;

import fileUploader.account.UserAccount;
import org.example.proiectpip2.infra.LocalUserDatabase;

public class UserService {

    static {
        LocalUserDatabase.initialize();
    }

    public static boolean addUser(User user) {
        return LocalUserDatabase.insertUser(user);
    }

    public static User findUser(String username, String password) {
        return LocalUserDatabase.findUser(username, password);
    }

    public static User ensureFaceUserByEmail(String email) {
        return LocalUserDatabase.ensureFaceUserByEmail(email);
    }

    public static UserAccount loadUserAccountByEmail(String email) {
        return LocalUserDatabase.loadAccountByEmail(email);
    }

    public static void saveUserAccountByEmail(String email, UserAccount account) {
        LocalUserDatabase.saveAccountByEmail(email, account);
    }

    public static void deleteUserByEmail(String email) {
        LocalUserDatabase.deleteAccountByEmail(email);
    }
}
