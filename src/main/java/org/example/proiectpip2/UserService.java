package org.example.proiectpip2;

import fileUploader.account.UserAccount;
import org.example.proiectpip2.infra.LocalUserDatabase;

/**
 * Clasă responsabilă pentru
 * gestionarea utilizatorilor.
 *
 * Această clasă oferă metode
 * pentru:
 * adăugarea utilizatorilor,
 * autentificare,
 * gestionarea conturilor și
 * integrarea Face ID.
 */
public class UserService {

    /**
     * Inițializează baza de date locală
     * la încărcarea clasei.
     */
    static {

        LocalUserDatabase.initialize();
    }

    /**
     * Adaugă un utilizator nou
     * în baza de date.
     *
     * @param user utilizatorul care trebuie adăugat
     * @return true dacă utilizatorul a fost adăugat,
     * false în caz contrar
     */
    public static boolean addUser(User user) {

        return LocalUserDatabase.insertUser(user);
    }

    /**
     * Caută un utilizator
     * după username și parolă.
     *
     * @param username username-ul utilizatorului
     * @param password parola utilizatorului
     * @return utilizatorul găsit
     * sau null dacă nu există
     */
    public static User findUser(
            String username,
            String password
    ) {

        return LocalUserDatabase.findUser(
                username,
                password
        );
    }

    /**
     * Creează sau returnează
     * un utilizator asociat
     * autentificării Face ID.
     *
     * @param email email-ul utilizatorului
     * @return utilizatorul asociat email-ului
     */
    public static User ensureFaceUserByEmail(String email) {

        return LocalUserDatabase.ensureFaceUserByEmail(email);
    }

    /**
     * Încarcă informațiile contului
     * asociat unui utilizator.
     *
     * @param email email-ul utilizatorului
     * @return obiectul UserAccount asociat
     */
    public static UserAccount loadUserAccountByEmail(String email) {

        return LocalUserDatabase.loadAccountByEmail(email);
    }

    /**
     * Salvează informațiile contului
     * unui utilizator.
     *
     * @param email email-ul utilizatorului
     * @param account contul utilizatorului
     */
    public static void saveUserAccountByEmail(
            String email,
            UserAccount account
    ) {

        LocalUserDatabase.saveAccountByEmail(
                email,
                account
        );
    }

    /**
     * Șterge contul unui utilizator
     * pe baza email-ului.
     *
     * @param email email-ul utilizatorului
     */
    public static void deleteUserByEmail(String email) {

        LocalUserDatabase.deleteAccountByEmail(email);
    }
}