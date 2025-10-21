import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class MealPlanPanel extends JPanel {

    private JComboBox<String> dayBox;
    private JComboBox<String> mealBox;
    private DefaultListModel<String> mealListModel = new DefaultListModel<>();
    private JList<String> mealList = new JList<>(mealListModel);

    private List<Recipe> recipes = new ArrayList<>();
    private Map<String, List<String>> mealsByDay = new HashMap<>();

    private List<Consumer<Void>> mealChangeListeners = new ArrayList<>();

    private static final String[] DAYS = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};

    public MealPlanPanel(List<Recipe> recipes) {
        this.recipes = recipes;
        setLayout(new BorderLayout());
        setBackground(new Color(34,34,34));

        // Top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(34,34,34));

        dayBox = new JComboBox<>(DAYS);
        mealBox = new JComboBox<>();
        updateMealBox();

        JButton addMealBtn = new JButton("Add Meal");
        addMealBtn.addActionListener(e -> addMealToDay());

        JButton deleteMealBtn = new JButton("Delete Meal"); // NEW BUTTON
        deleteMealBtn.addActionListener(e -> deleteSelectedMeal());

        topPanel.add(new JLabel("Day:"));
        topPanel.add(dayBox);
        topPanel.add(new JLabel("Meal:"));
        topPanel.add(mealBox);
        topPanel.add(addMealBtn);
        topPanel.add(deleteMealBtn); // Add button to panel

        add(topPanel, BorderLayout.NORTH);

        // Meal list
        mealList.setBackground(new Color(44,44,44));
        mealList.setForeground(Color.GREEN);
        JScrollPane scroll = new JScrollPane(mealList);
        add(scroll, BorderLayout.CENTER);

        // Initialize empty meals
        for (String day : DAYS) {
            mealsByDay.put(day, new ArrayList<>());
        }
    }

    public void refreshRecipes(List<Recipe> filtered) {
        this.recipes = filtered;
        updateMealBox();
    }

    private void updateMealBox() {
        String[] names = recipes.stream().map(Recipe::getName).toArray(String[]::new);
        mealBox.setModel(new DefaultComboBoxModel<>(names));
    }

    private void addMealToDay() {
        String day = (String) dayBox.getSelectedItem();
        String meal = (String) mealBox.getSelectedItem();
        if(day != null && meal != null){
            mealsByDay.get(day).add(meal);
            updateMealList(day);
            notifyMealChange();
        }
    }

    // NEW METHOD: Delete selected meal from the list
    private void deleteSelectedMeal() {
        String day = (String) dayBox.getSelectedItem();
        int selectedIndex = mealList.getSelectedIndex();
        if(day != null && selectedIndex >= 0){
            mealsByDay.get(day).remove(selectedIndex);
            updateMealList(day);
            notifyMealChange();
        }
    }

    private void updateMealList(String day){
        mealListModel.clear();
        for(String meal : mealsByDay.get(day)){
            mealListModel.addElement(meal);
        }
    }

    public List<String> getMealsForDay(String day){
        return mealsByDay.getOrDefault(day,new ArrayList<>());
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void addMealChangeListener(Runnable listener){
        mealChangeListeners.add(v -> listener.run());
    }

    private void notifyMealChange(){
        for(Consumer<Void> c : mealChangeListeners){
            c.accept(null);
        }
    }
}
