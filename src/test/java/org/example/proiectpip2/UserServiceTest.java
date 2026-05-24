package org.example.proiectpip2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Test
    void testAddAndFindUser() {
        String username = "user_" + System.nanoTime();
        User user = new User(username, username + "@student.tuiasi.ro", "12345678", "JUNIOR");

        boolean inserted = UserService.addUser(user);
        assertTrue(inserted);

        User found = UserService.findUser(username, "12345678");
        assertNotNull(found);
        assertEquals(username, found.getUsername());
    }

    @Test
    void testFindUserWrongPassword() {
        String username = "user_" + System.nanoTime();
        User user = new User(username, username + "@student.tuiasi.ro", "12345678", "JUNIOR");
        UserService.addUser(user);

        User found = UserService.findUser(username, "wrong");
        assertNull(found);
    }

    @Test
    void testDuplicateUsernameRejected() {
        String username = "user_" + System.nanoTime();
        User user1 = new User(username, username + "@student.tuiasi.ro", "12345678", "JUNIOR");
        User user2 = new User(username, "other@student.tuiasi.ro", "12345678", "SENIOR");

        assertTrue(UserService.addUser(user1));
        assertFalse(UserService.addUser(user2));
    }
}
