package fileUploader.account;

/**
 * Reprezintă un cont de utilizator
 * din aplicația SmartDocs.
 *
 * Clasa stochează informații despre utilizator,
 * precum numele, poziția, vârsta,
 * performanța și avatarul selectat.
 */
public class UserAccount {

    /**
     * Prenumele utilizatorului.
     */
    private String firstName;

    /**
     * Numele utilizatorului.
     */
    private String lastName;

    /**
     * Poziția utilizatorului.
     */
    private String position;

    /**
     * Vârsta utilizatorului.
     */
    private int age;

    /**
     * Nivelul de performanță al utilizatorului.
     */
    private int performance;

    /**
     * ID-ul utilizatorului.
     */
    private int userId;

    /**
     * Indexul avatarului selectat.
     */
    private int selectedAvatarIndex;

    /**
     * Creează un obiect de tip UserAccount.
     *
     * @param firstName prenumele utilizatorului
     * @param lastName numele utilizatorului
     * @param position poziția utilizatorului
     * @param age vârsta utilizatorului
     * @param performance performanța utilizatorului
     * @param userId ID-ul utilizatorului
     */
    public UserAccount(String firstName, String lastName, String position, int age, int performance, int userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.age = age;
        this.performance = performance;
        this.userId = userId;
        this.selectedAvatarIndex = 0;
    }

    /**
     * Returnează prenumele utilizatorului.
     *
     * @return prenumele utilizatorului
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Returnează numele utilizatorului.
     *
     * @return numele utilizatorului
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Returnează vârsta utilizatorului.
     *
     * @return vârsta utilizatorului
     */
    public int getAge() {
        return this.age;
    }

    /**
     * Returnează poziția utilizatorului.
     *
     * @return poziția utilizatorului
     */
    public String getPosition() {
        return this.position;
    }

    /**
     * Returnează performanța utilizatorului.
     *
     * @return performanța utilizatorului
     */
    public int getPerformance() {
        return this.performance;
    }

    /**
     * Returnează indexul avatarului selectat.
     *
     * @return indexul avatarului
     */
    public int getSelectedAvatarIndex() {
        return this.selectedAvatarIndex;
    }

    /**
     * Modifică vârsta utilizatorului.
     *
     * @param age noua vârstă
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Modifică poziția utilizatorului.
     *
     * @param position noua poziție
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * Modifică performanța utilizatorului.
     *
     * @param performance noua performanță
     */
    public void setPerformance(int performance) {
        this.performance = performance;
    }

    /**
     * Modifică prenumele utilizatorului.
     *
     * @param firstName noul prenume
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Modifică numele utilizatorului.
     *
     * @param lastName noul nume
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Modifică avatarul selectat.
     *
     * @param selectedAvatarIndex indexul noului avatar
     */
    public void setSelectedAvatarIndex(int selectedAvatarIndex) {
        this.selectedAvatarIndex = selectedAvatarIndex;
    }
}