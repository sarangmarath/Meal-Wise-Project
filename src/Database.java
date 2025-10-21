import java.sql.*;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/mealwise?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";      
    private static final String PASSWORD = "12345"; 

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement()) {

                // === Users Table ===
                stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(50) NOT NULL UNIQUE," +
                    "password VARCHAR(100) NOT NULL," +
                    "weight DOUBLE," +
                    "height DOUBLE," +
                    "age INT" +
                    ")"
                );
                System.out.println(" Users table ready");

                // === Recipes Table ===
                stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS recipes (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL," +
                    "ingredients TEXT NOT NULL," +
                    "calories INT NOT NULL," +
                    "dietary_type VARCHAR(50) NOT NULL" +
                    ")"
                );
                System.out.println(" Recipes table ready");

                // === Meal Plans Table ===
                stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS meal_plans (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_id INT NOT NULL," +
                    "recipe_id INT NOT NULL," +
                    "plan_date DATE NOT NULL," +
                    "meal_type VARCHAR(50)," +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE," +
                    "FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE" +
                    ")"
                );
                System.out.println(" Meal Plans table ready");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
