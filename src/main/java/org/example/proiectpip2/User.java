package org.example.proiectpip2;

/**
 * Reprezintă un utilizator
 * al aplicației SmartDocs.
 *
 * Clasa stochează informațiile
 * necesare autentificării și
 * identificării utilizatorului.
 */
public class User {

    /**
     * Username-ul utilizatorului.
     */
    private String username;

    /**
     * Email-ul utilizatorului.
     */
    private String email;

    /**
     * Parola utilizatorului.
     */
    private String password;

    /**
     * Rolul utilizatorului.
     */
    private String role;

    /**
     * Creează un obiect de tip User.
     *
     * @param username username-ul utilizatorului
     * @param email email-ul utilizatorului
     * @param password parola utilizatorului
     * @param role rolul utilizatorului
     */
    public User(
            String username,
            String email,
            String password,
            String role
    ) {

        this.username = username;

        this.email = email;

        this.password = password;

        this.role = role;
    }

    /**
     * Returnează username-ul utilizatorului.
     *
     * @return username-ul utilizatorului
     */
    public String getUsername() {

        return username;
    }

    /**
     * Modifică username-ul utilizatorului.
     *
     * @param username noul username
     */
    public void setUsername(String username) {

        this.username = username;
    }

    /**
     * Returnează email-ul utilizatorului.
     *
     * @return email-ul utilizatorului
     */
    public String getEmail() {

        return email;
    }

    /**
     * Modifică email-ul utilizatorului.
     *
     * @param email noul email
     */
    public void setEmail(String email) {

        this.email = email;
    }

    /**
     * Returnează parola utilizatorului.
     *
     * @return parola utilizatorului
     */
    public String getPassword() {

        return password;
    }

    /**
     * Modifică parola utilizatorului.
     *
     * @param password noua parolă
     */
    public void setPassword(String password) {

        this.password = password;
    }

    /**
     * Returnează rolul utilizatorului.
     *
     * @return rolul utilizatorului
     */
    public String getRole() {

        return role;
    }

    /**
     * Modifică rolul utilizatorului.
     *
     * @param role noul rol
     */
    public void setRole(String role) {

        this.role = role;
    }
}