import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CalendarPanel extends JPanel {

    private JTable calendarTable;
    private DefaultTableModel tableModel;
    private MealPlanPanel mealPlanPanel;

    private static final String[] DAYS = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};

    public CalendarPanel(MealPlanPanel mealPlanPanel) {
        this.mealPlanPanel = mealPlanPanel;
        setLayout(new BorderLayout());
        setBackground(new Color(34,34,34));

        String[] columns = {"Day", "Meals", "Total Calories"};
        tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        calendarTable = new JTable(tableModel);
        calendarTable.setBackground(new Color(44,44,44));
        calendarTable.setForeground(Color.GREEN);
        calendarTable.getTableHeader().setBackground(new Color(34,34,34));
        calendarTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(calendarTable);
        add(scroll, BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {
        tableModel.setRowCount(0);

        for(String day : DAYS){
            List<String> meals = mealPlanPanel.getMealsForDay(day);

            int totalCalories = 0;
            StringBuilder mealText = new StringBuilder();

            for(String mealName : meals){
                mealText.append(mealName).append(", ");
                for(Recipe r : mealPlanPanel.getRecipes()){
                    if(r.getName().equals(mealName)){
                        totalCalories += r.getCalories();
                        break;
                    }
                }
            }

            if(mealText.length() > 2){
                mealText.setLength(mealText.length() - 2);
            }

            tableModel.addRow(new Object[]{day, mealText.toString(), totalCalories});
        }
    }
    // Getter for PDF export
    public JTable getCalendarTable() {
        return calendarTable;
    }
}
