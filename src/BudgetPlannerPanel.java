import javax.swing.*;
import java.awt.*;

public class BudgetPlannerPanel extends JPanel {
    private JTextField[] costFields = new JTextField[7];
    private JLabel totalLabel = new JLabel("Total Weekly Budget: $0.00");
    private static final String[] DAYS = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};

    public BudgetPlannerPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(34,34,34));

        JPanel inputPanel = new JPanel(new GridLayout(7,2,5,5));
        inputPanel.setBackground(new Color(34,34,34));

        for(int i=0;i<7;i++){
            JLabel dayLabel = new JLabel(DAYS[i] + ":");
            dayLabel.setForeground(Color.GREEN);
            costFields[i] = new JTextField();
            inputPanel.add(dayLabel);
            inputPanel.add(costFields[i]);
        }

        JButton calculateBtn = new JButton("Calculate Total");
        calculateBtn.addActionListener(e -> calculateBudget());

        totalLabel.setForeground(Color.WHITE);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(34,34,34));
        bottomPanel.add(calculateBtn, BorderLayout.NORTH);
        bottomPanel.add(totalLabel, BorderLayout.CENTER);

        add(inputPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void calculateBudget(){
        double total = 0;
        for(JTextField field : costFields){
            try{
                total += Double.parseDouble(field.getText());
            }catch(Exception e){
                // ignore invalid input
            }
        }
        totalLabel.setText("Total Weekly Budget: $" + String.format("%.2f", total));
    }
}
