package fileUploader.account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserAccountTest {

    @Test
    void testConstructor() {

        UserAccount user = new UserAccount(
                "Andrei",
                "Boscu",
                "Developer",
                21,
                95,
                1
        );

        assertNotNull(user);
    }

    @Test
    void testGetFirstName() {

        UserAccount user = new UserAccount(
                "Andrei",
                "Boscu",
                "Developer",
                21,
                95,
                1
        );

        assertEquals(
                "Andrei",
                user.getFirstName()
        );
    }

    @Test
    void testGetLastName() {

        UserAccount user = new UserAccount(
                "Andrei",
                "Boscu",
                "Developer",
                21,
                95,
                1
        );

        assertEquals(
                "Boscu",
                user.getLastName()
        );
    }

    @Test
    void testGetAge() {

        UserAccount user = new UserAccount(
                "Andrei",
                "Boscu",
                "Developer",
                21,
                95,
                1
        );

        assertEquals(
                21,
                user.getAge()
        );
    }

    @Test
    void testGetPosition() {

        UserAccount user = new UserAccount(
                "Andrei",
                "Boscu",
                "Developer",
                21,
                95,
                1
        );

        assertEquals(
                "Developer",
                user.getPosition()
        );
    }

    @Test
    void testGetPerformance() {

        UserAccount user = new UserAccount(
                "Andrei",
                "Boscu",
                "Developer",
                21,
                95,
                1
        );

        assertEquals(
                95,
                user.getPerformance()
        );
    }

    @Test
    void testDefaultAvatarIndex() {

        UserAccount user = new UserAccount(
                "Andrei",
                "Boscu",
                "Developer",
                21,
                95,
                1
        );

        assertEquals(
                0,
                user.getSelectedAvatarIndex()
        );
    }

    @Test
    void testSetFirstName() {

        UserAccount user = new UserAccount(
                "Andrei",
                "Boscu",
                "Developer",
                21,
                95,
                1
        );

        user.setFirstName("Maria");

        assertEquals(
                "Maria",
                user.getFirstName()
        );
    }

    @Test
    void testSetLastName() {

        UserAccount user = new UserAccount(
                "Andrei",
                "Boscu",
                "Developer",
                21,
                95,
                1
        );

        user.setLastName("Popescu");

        assertEquals(
                "Popescu",
                user.getLastName()
        );
    }

    @Test
    void testSetAge() {

        UserAccount user = new UserAccount(
                "Andrei",
                "Boscu",
                "Developer",
                21,
                95,
                1
        );

        user.setAge(30);

        assertEquals(
                30,
                user.getAge()
        );
    }

    @Test
    void testSetPosition() {

        UserAccount user = new UserAccount(
                "Andrei",
                "Boscu",
                "Developer",
                21,
                95,
                1
        );

        user.setPosition("Manager");

        assertEquals(
                "Manager",
                user.getPosition()
        );
    }

    @Test
    void testSetPerformance() {

        UserAccount user = new UserAccount(
                "Andrei",
                "Boscu",
                "Developer",
                21,
                95,
                1
        );

        user.setPerformance(100);

        assertEquals(
                100,
                user.getPerformance()
        );
    }

    @Test
    void testSetAvatarIndex() {

        UserAccount user = new UserAccount(
                "Andrei",
                "Boscu",
                "Developer",
                21,
                95,
                1
        );

        user.setSelectedAvatarIndex(3);

        assertEquals(
                3,
                user.getSelectedAvatarIndex()
        );
    }

    @Test
    void testTwoObjectsAreDifferent() {

        UserAccount user1 = new UserAccount(
                "A",
                "B",
                "Dev",
                20,
                90,
                1
        );

        UserAccount user2 = new UserAccount(
                "A",
                "B",
                "Dev",
                20,
                90,
                1
        );

        assertNotSame(user1, user2);
    }
}