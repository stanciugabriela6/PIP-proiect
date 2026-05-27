package org.example.proiectpip2.infra;

import fileUploader.account.UserAccount;
import org.example.proiectpip2.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.Base64;

public final class LocalUserDatabase {

    private static final String DB_URL = "jdbc:sqlite:" + Path.of(System.getProperty("user.home"), ".smartdocs", "smartdocs.db");

    private LocalUserDatabase() {
    }

    static {
        try {
            java.nio.file.Files.createDirectories(Path.of(System.getProperty("user.home"), ".smartdocs"));
            initialize();
        } catch (Exception e) {
            throw new RuntimeException("Cannot initialize local DB", e);
        }
    }

    public static void initialize() {
        String ddl = """
                CREATE TABLE IF NOT EXISTS users (
                    username TEXT PRIMARY KEY,
                    email TEXT NOT NULL,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL,
                    first_name TEXT,
                    last_name TEXT,
                    position TEXT,
                    age INTEGER DEFAULT 0,
                    performance INTEGER DEFAULT 0,
                    avatar_index INTEGER DEFAULT 0
                )
                """;

        try (Connection c = DriverManager.getConnection(DB_URL);
             Statement s = c.createStatement()) {
            s.execute(ddl);
            s.execute("CREATE UNIQUE INDEX IF NOT EXISTS idx_users_email ON users(email)");
        } catch (SQLException e) {
            throw new RuntimeException("DB init failed", e);
        }
    }

    public static boolean insertUser(User user) {
        String sql = """
                INSERT INTO users(username,email,password,role,first_name,last_name,position,age,performance,avatar_index)
                VALUES(?,?,?,?,?,?,?,?,?,?)
                """;
        try (Connection c = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, hashPassword(user.getPassword())); // stocat ca hash, niciodata plain-text
            ps.setString(4, user.getRole());
            ps.setString(5, user.getUsername());
            ps.setString(6, "");
            ps.setString(7, user.getRole());
            ps.setInt(8, 0);
            ps.setInt(9, 0);
            ps.setInt(10, 0);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static User findUser(String username, String password) {
        // Cautam doar dupa username; verificarea parolei se face in Java
        // (nu in SQL) pentru a putea compara constant-time si a evita
        // expunerea hash-ului in query.
        String sql = "SELECT username,email,password,role FROM users WHERE username=?";
        try (Connection c = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    if (verifyPassword(password, storedHash)) {
                        return new User(
                                rs.getString("username"),
                                rs.getString("email"),
                                storedHash,
                                rs.getString("role"));
                    }
                }
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public static User ensureFaceUserByEmail(String email) {
        String find = "SELECT username,email,password,role FROM users WHERE email=?";
        try (Connection c = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = c.prepareStatement(find)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getString("username"), rs.getString("email"), rs.getString("password"), rs.getString("role"));
                }
            }
        } catch (SQLException ignored) {
        }

        String username = email.contains("@") ? email.substring(0, email.indexOf('@')) : email;
        String candidate = username;
        int i = 1;
        while (usernameExists(candidate)) {
            candidate = username + "_" + i++;
        }

        User newUser = new User(candidate, email, "FACE_ID_ONLY", "EMPLOYEE");
        insertUser(newUser);
        return newUser;
    }

    private static boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username=?";
        try (Connection c = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public static UserAccount loadAccountByEmail(String email) {
        String sql = "SELECT first_name,last_name,position,age,performance,avatar_index FROM users WHERE email=?";
        try (Connection c = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return new UserAccount("", "", "Employee", 0, 0, 0);
                UserAccount acc = new UserAccount(
                        rs.getString("first_name") == null ? "" : rs.getString("first_name"),
                        rs.getString("last_name") == null ? "" : rs.getString("last_name"),
                        rs.getString("position") == null ? "Employee" : rs.getString("position"),
                        rs.getInt("age"),
                        rs.getInt("performance"),
                        0
                );
                acc.setSelectedAvatarIndex(rs.getInt("avatar_index"));
                return acc;
            }
        } catch (SQLException e) {
            return new UserAccount("", "", "Employee", 0, 0, 0);
        }
    }

    public static void saveAccountByEmail(String email, UserAccount account) {
        String sql = """
                UPDATE users
                SET first_name=?, last_name=?, position=?, age=?, performance=?, avatar_index=?
                WHERE email=?
                """;
        try (Connection c = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, account.getFirstName());
            ps.setString(2, account.getLastName());
            ps.setString(3, account.getPosition());
            ps.setInt(4, account.getAge());
            ps.setInt(5, account.getPerformance());
            ps.setInt(6, account.getSelectedAvatarIndex());
            ps.setString(7, email);
            ps.executeUpdate();
        } catch (SQLException ignored) {
        }
    }

    public static void deleteAccountByEmail(String email) {
        String sql = "DELETE FROM users WHERE email=?";
        try (Connection c = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.executeUpdate();
        } catch (SQLException ignored) {
        }
    }

    // ---------------------------------------------------------------
    // Hashing parole — PBKDF2-HMAC-SHA256, 310.000 iteratii (OWASP)
    // Format stocat: base64(salt):base64(hash)
    // ---------------------------------------------------------------

    static String hashPassword(String plainPassword) {
        try {
            byte[] salt = new byte[16];
            new SecureRandom().nextBytes(salt);
            PBEKeySpec spec = new PBEKeySpec(
                    plainPassword.toCharArray(), salt, 310_000, 256);
            SecretKeyFactory skf =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            spec.clearPassword();
            return Base64.getEncoder().encodeToString(salt)
                    + ":" + Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Eroare la hashing parola", e);
        }
    }

    static boolean verifyPassword(String plainPassword, String stored) {
        try {
            String[] parts = stored.split(":", 2);
            if (parts.length != 2) return false;
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[1]);
            PBEKeySpec spec = new PBEKeySpec(
                    plainPassword.toCharArray(), salt, 310_000, 256);
            SecretKeyFactory skf =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] actualHash = skf.generateSecret(spec).getEncoded();
            spec.clearPassword();
            // comparatie constant-time — previne timing attacks
            return MessageDigest.isEqual(expectedHash, actualHash);
        } catch (Exception e) {
            return false;
        }
    }
}