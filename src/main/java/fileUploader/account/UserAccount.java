package fileUploader.account;

public class UserAccount {
    private String firstName;
    private String lastName;
    private String position;
    private int age;
    private int performance;
    private int userId;
    private int selectedAvatarIndex;

    public UserAccount(String firstName, String lastName, String position, int age, int performance, int userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.age = age;
        this.performance = performance;
        this.userId = userId;
        this.selectedAvatarIndex = 0;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public int getAge() {
        return this.age;
    }

    public String getPosition() {
        return this.position;
    }

    public int getPerformance() {
        return this.performance;
    }

    public int getSelectedAvatarIndex() {
        return this.selectedAvatarIndex;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setPerformance(int performance) {
        this.performance = performance;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSelectedAvatarIndex(int selectedAvatarIndex) {
        this.selectedAvatarIndex = selectedAvatarIndex;
    }
}