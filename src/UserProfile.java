public class UserProfile {
    private String username;

    // Optional fields
    private int id;
    private int age;
    private double weight;
    private double height;

    // Constructor for login with full profile
    public UserProfile(int id, String username, int age, double weight, double height) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.weight = weight;
        this.height = height;
    }

    // Simple constructor if only username is known
    public UserProfile(String username) {
        this.username = username;
    }

    public String getUsername() { return username; }
    public int getId() { return id; }
    public int getAge() { return age; }
    public double getWeight() { return weight; }
    public double getHeight() { return height; }
}
