import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.sql.*;

public class AuthFrame extends JFrame {
    private Image bgImage;

    public AuthFrame() {
        setTitle("MealWise - Login or Register");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Load background image
        try {
            File imgFile = new File("meal.jpg.jpeg"); // adjust path
            if (!imgFile.exists()) {
                System.err.println(" Image file not found: " + imgFile.getAbsolutePath());
            } else {
                bgImage = ImageIO.read(imgFile);
                System.out.println(" Background image loaded successfully");
            }
        } catch (IOException e) {
            System.err.println(" Failed to load background image: " + e.getMessage());
        }

        // Background panel
        JPanel backgroundPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        // Transparent glass panel
        JPanel glassPanel = new JPanel(new BorderLayout()) {
            @Override
            public boolean isOpaque() { return false; }
        };
        glassPanel.setBackground(new Color(255, 255, 255, 210));
        glassPanel.setPreferredSize(new Dimension(420, 350));
        glassPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setForeground(Color.BLACK);

        tabbedPane.add("Login", createLoginPanel());
        tabbedPane.add("Register", createRegisterPanel());

        JLabel title = new JLabel("MealWise", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.BLACK);
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        glassPanel.add(title, BorderLayout.NORTH);
        glassPanel.add(tabbedPane, BorderLayout.CENTER);

        backgroundPanel.add(glassPanel);
        setContentPane(backgroundPanel);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = createLabel("Username:");
        JTextField usernameField = createField();

        JLabel passLabel = createLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);
        styleField(passwordField);

        JButton loginBtn = createButton("Login");
        loginBtn.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Login with database
            try (Connection conn = Database.getConnection();
                 PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
                ps.setString(1, user);
                ps.setString(2, pass);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("id");
                    int age = rs.getInt("age");
                    double weight = rs.getDouble("weight");
                    double height = rs.getDouble("height");

                    UserProfile loggedInUser = new UserProfile(id, user, age, weight, height);

                    JOptionPane.showMessageDialog(this, "Login successful! Welcome " + user);
                    dispose();
                    new MainWindow(loggedInUser).setVisible(true); // Your main window
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        c.gridx = 0; c.gridy = 0;
        panel.add(userLabel, c);
        c.gridx = 1;
        panel.add(usernameField, c);

        c.gridx = 0; c.gridy = 1;
        panel.add(passLabel, c);
        c.gridx = 1;
        panel.add(passwordField, c);

        c.gridx = 0; c.gridy = 2; c.gridwidth = 2;
        panel.add(loginBtn, c);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = createLabel("Username:");
        JTextField usernameField = createField();

        JLabel passLabel = createLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);
        styleField(passwordField);

        JLabel weightLabel = createLabel("Weight (kg):");
        JTextField weightField = createField();

        JLabel heightLabel = createLabel("Height (cm):");
        JTextField heightField = createField();

        JLabel ageLabel = createLabel("Age:");
        JTextField ageField = createField();

        JButton registerBtn = createButton("Register");
        registerBtn.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());
            String weight = weightField.getText();
            String height = heightField.getText();
            String age = ageField.getText();

            if (user.isEmpty() || pass.isEmpty() || weight.isEmpty() || height.isEmpty() || age.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = Database.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "INSERT INTO users (username, password, weight, height, age) VALUES (?, ?, ?, ?, ?)")) {
                ps.setString(1, user);
                ps.setString(2, pass);
                ps.setDouble(3, Double.parseDouble(weight));
                ps.setDouble(4, Double.parseDouble(height));
                ps.setInt(5, Integer.parseInt(age));

                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Registration successful for " + user);
            } catch (SQLException ex) {
                if (ex.getErrorCode() == 1062) { // duplicate
                    JOptionPane.showMessageDialog(this, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        int y = 0;
        c.gridx = 0; c.gridy = y; panel.add(userLabel, c);
        c.gridx = 1; panel.add(usernameField, c);

        c.gridy = ++y; c.gridx = 0; panel.add(passLabel, c);
        c.gridx = 1; panel.add(passwordField, c);

        c.gridy = ++y; c.gridx = 0; panel.add(weightLabel, c);
        c.gridx = 1; panel.add(weightField, c);

        c.gridy = ++y; c.gridx = 0; panel.add(heightLabel, c);
        c.gridx = 1; panel.add(heightField, c);

        c.gridy = ++y; c.gridx = 0; panel.add(ageLabel, c);
        c.gridx = 1; panel.add(ageField, c);

        c.gridy = ++y; c.gridx = 0; c.gridwidth = 2;
        panel.add(registerBtn, c);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.BLACK);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }

    private JTextField createField() {
        JTextField field = new JTextField(15);
        styleField(field);
        return field;
    }

    private void styleField(JTextField field) {
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setCaretColor(Color.BLACK);
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(230, 230, 230));
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AuthFrame().setVisible(true));
    }
}
