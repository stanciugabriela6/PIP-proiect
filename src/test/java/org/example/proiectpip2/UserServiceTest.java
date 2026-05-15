package org.example.proiectpip2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Test
    void testAddUser() {
        UserService.users.clear();

        User user = new User(
                "Andrei",
                "andrei@student.tuiasi.ro",
                "12345678",
                "JUNIOR"
        );

        UserService.addUser(user);

        assertEquals(1, UserService.users.size());
    }

    @Test
    void testFindUserCorrect() {
        UserService.users.clear();

        User user = new User(
                "Andrei",
                "andrei@student.tuiasi.ro",
                "12345678",
                "JUNIOR"
        );

        UserService.addUser(user);

        User result = UserService.findUser("Andrei", "12345678");

        assertNotNull(result);
        assertEquals("Andrei", result.getUsername());
    }

    @Test
    void testFindUserWrongPassword() {
        UserService.users.clear();

        User user = new User(
                "Andrei",
                "andrei@student.tuiasi.ro",
                "12345678",
                "JUNIOR"
        );

        UserService.addUser(user);

        User result = UserService.findUser("Andrei", "gresit");

        assertNull(result);
    }

    @Test
    void testUsersListInitiallyEmpty() {
        UserService.users.clear();

        assertEquals(0, UserService.users.size());
    }

    @Test
    void testAddMultipleUsers() {
        UserService.users.clear();

        User user1 = new User(
                "Andrei",
                "andrei@student.tuiasi.ro",
                "12345678",
                "JUNIOR"
        );

        User user2 = new User(
                "Maria",
                "maria@student.tuiasi.ro",
                "87654321",
                "SENIOR"
        );

        UserService.addUser(user1);
        UserService.addUser(user2);

        assertEquals(2, UserService.users.size());
    }

    @Test
    void testFindUserWrongUsername() {
        UserService.users.clear();

        User user = new User(
                "Andrei",
                "andrei@student.tuiasi.ro",
                "12345678",
                "JUNIOR"
        );

        UserService.addUser(user);

        User result = UserService.findUser("Gigel", "12345678");

        assertNull(result);
    }

    @Test
    void testFindUserInEmptyList() {
        UserService.users.clear();

        User result = UserService.findUser("Andrei", "12345678");

        assertNull(result);
    }

    @Test
    void testAddUserWithEmptyUsername() {

        UserService.users.clear();

        User user = new User(
                "",
                "test@student.tuiasi.ro",
                "12345678",
                "JUNIOR"
        );

        UserService.addUser(user);

        assertEquals(1, UserService.users.size());
    }

    @Test
    void testFindUserCaseSensitive() {

        UserService.users.clear();

        User user = new User(
                "Andrei",
                "andrei@student.tuiasi.ro",
                "12345678",
                "JUNIOR"
        );

        UserService.addUser(user);

        User result = UserService.findUser("andrei", "12345678");

        assertNull(result);
    }

    @Test
    void testAddDuplicateUsers() {

        UserService.users.clear();

        User user1 = new User(
                "Andrei",
                "andrei@student.tuiasi.ro",
                "12345678",
                "JUNIOR"
        );

        User user2 = new User(
                "Andrei",
                "andrei@student.tuiasi.ro",
                "12345678",
                "JUNIOR"
        );

        UserService.addUser(user1);
        UserService.addUser(user2);

        assertEquals(2, UserService.users.size());
    }

    @Test
    void testFindSecondUserFromList() {

        UserService.users.clear();

        User user1 = new User(
                "Andrei",
                "andrei@student.tuiasi.ro",
                "12345678",
                "JUNIOR"
        );

        User user2 = new User(
                "Maria",
                "maria@student.tuiasi.ro",
                "87654321",
                "SENIOR"
        );

        UserService.addUser(user1);
        UserService.addUser(user2);

        User result = UserService.findUser("Maria", "87654321");

        assertNotNull(result);
        assertEquals("Maria", result.getUsername());
    }

    @Test
    void testFindUserWithWrongPasswordAndWrongUsername() {

        UserService.users.clear();

        User user = new User(
                "Andrei",
                "andrei@student.tuiasi.ro",
                "12345678",
                "JUNIOR"
        );

        UserService.addUser(user);

        User result = UserService.findUser("Gigel", "parola");

        assertNull(result);
    }
}