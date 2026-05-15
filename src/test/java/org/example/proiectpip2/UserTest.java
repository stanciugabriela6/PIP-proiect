package org.example.proiectpip2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testUserConstructor() {

        User user = new User(
                "Andrei",
                "andrei@student.tuiasi.ro",
                "12345678",
                "JUNIOR"
        );

        assertEquals("Andrei", user.getUsername());
        assertEquals("andrei@student.tuiasi.ro", user.getEmail());
        assertEquals("12345678", user.getPassword());
    }

    @Test
    void testSetUsername() {

        User user = new User(
                "Andrei",
                "andrei@student.tuiasi.ro",
                "12345678",
                "JUNIOR"
        );

        user.setUsername("Maria");

        assertEquals("Maria", user.getUsername());
    }

    @Test
    void testSetEmail() {

        User user = new User(
                "Andrei",
                "andrei@student.tuiasi.ro",
                "12345678",
                "JUNIOR"
        );

        user.setEmail("maria@student.tuiasi.ro");

        assertEquals("maria@student.tuiasi.ro", user.getEmail());
    }

    @Test
    void testSetPassword() {

        User user = new User(
                "Andrei",
                "andrei@student.tuiasi.ro",
                "12345678",
                "JUNIOR"
        );

        user.setPassword("87654321");

        assertEquals("87654321", user.getPassword());
    }
    @Test
    void testEmptyUsername() {

        User user = new User(
                "",
                "test@student.tuiasi.ro",
                "12345678",
                "JUNIOR"
        );

        assertEquals("", user.getUsername());
    }

    @Test
    void testEmptyEmail() {

        User user = new User(
                "Andrei",
                "",
                "12345678",
                "JUNIOR"
        );

        assertEquals("", user.getEmail());
    }

    @Test
    void testChangePasswordMultipleTimes() {

        User user = new User(
                "Andrei",
                "andrei@student.tuiasi.ro",
                "12345678",
                "JUNIOR"
        );

        user.setPassword("aaaa");
        user.setPassword("bbbb");

        assertEquals("bbbb", user.getPassword());
    }

    @Test
    void testSpecialCharactersInUsername() {

        User user = new User(
                "Andrei_123",
                "andrei@student.tuiasi.ro",
                "12345678",
                "JUNIOR"
        );

        assertEquals("Andrei_123", user.getUsername());
    }
    @Test
    void testUserObjectIsNotNull() {

        User user = new User(
                "Andrei",
                "andrei@student.tuiasi.ro",
                "12345678",
                "JUNIOR"
        );

        assertNotNull(user);
    }

    @Test
    void testUsernameIsCaseSensitive() {

        User user = new User(
                "Andrei",
                "andrei@student.tuiasi.ro",
                "12345678",
                "JUNIOR"
        );

        assertNotEquals("andrei", user.getUsername());
    }

    @Test
    void testEmailContainsStudentDomain() {

        User user = new User(
                "Andrei",
                "andrei@student.tuiasi.ro",
                "12345678",
                "JUNIOR"
        );

        assertTrue(
                user.getEmail().contains("@student.tuiasi.ro")
        );
    }

    @Test
    void testPasswordLength() {

        User user = new User(
                "Andrei",
                "andrei@student.tuiasi.ro",
                "12345678",
                "JUNIOR"
        );

        assertTrue(user.getPassword().length() >= 8);
    }

    @Test
    void testTwoUsersAreDifferentObjects() {

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

        assertNotSame(user1, user2);
    }

    @Test
    void testUserClassName() {

        User user = new User(
                "Andrei",
                "andrei@student.tuiasi.ro",
                "12345678",
                "JUNIOR"
        );

        assertEquals(
                "User",
                user.getClass().getSimpleName()
        );
    }
}
