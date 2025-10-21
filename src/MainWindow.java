import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainWindow extends JFrame {
    private UserProfile user;

    public MainWindow(UserProfile user) {
        this.user = user;

        setTitle("MealWise - Meal Planner");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        Color darkGray = new Color(34, 34, 34);
        Color darkGreen = new Color(0, 255, 0);
        getContentPane().setBackground(darkGray);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("MENU");
        JMenuItem logoutItem = new JMenuItem("Log Out");
        fileMenu.add(logoutItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        logoutItem.addActionListener(e -> {
            dispose();
            new AuthFrame().setVisible(true);
        });

        // Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(darkGray);
        tabs.setForeground(darkGreen);

        // Recipe Manager Panel
        RecipeManagerPanel recipePanel = new RecipeManagerPanel();

        // Meal Plan Panel
        MealPlanPanel mealPlanPanel = new MealPlanPanel(recipePanel.getFilteredRecipeList());

        // Calendar Panel
        CalendarPanel calendarPanel = new CalendarPanel(mealPlanPanel);

        //PDF
        PDFExportPanel pdfPanel = new PDFExportPanel(calendarPanel);


        // Other panels
        BudgetPlannerPanel budgetPanel = new BudgetPlannerPanel();
        BMICalculatorPanel bmiPanel = new BMICalculatorPanel();
    

        // Add tabs
        tabs.addTab("Recipes", recipePanel);
        tabs.addTab("Meal Plans", mealPlanPanel);
        tabs.addTab("Calendar", calendarPanel);
        tabs.addTab("Budget Planner", budgetPanel);
        tabs.addTab("BMI Calculator", bmiPanel);
        tabs.addTab("Export PDF", pdfPanel);


        // === Automatic Refreshes ===
        recipePanel.filterBox.addActionListener(e -> {
            List<Recipe> filteredRecipes = recipePanel.getFilteredRecipeList();
            mealPlanPanel.refreshRecipes(filteredRecipes);
            calendarPanel.refresh();
        });

        mealPlanPanel.addMealChangeListener(() -> calendarPanel.refresh());

        tabs.addChangeListener(e -> {
            int idx = tabs.getSelectedIndex();
            if ("Calendar".equals(tabs.getTitleAt(idx))) {
                calendarPanel.refresh();
            }
        });

        add(tabs, BorderLayout.CENTER);

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome, " + (user != null ? user.getUsername() : "Guest"), SwingConstants.CENTER);
        welcomeLabel.setForeground(darkGreen);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(welcomeLabel, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AuthFrame().setVisible(true));
    }
}
