import javax.swing.*;
import java.awt.*;

public class BMICalculatorPanel extends JPanel {
    private JTextField weightField = new JTextField();
    private JTextField heightField = new JTextField();
    private JLabel resultLabel = new JLabel("Your BMI: ");

    public BMICalculatorPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(34,34,34));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10,10,10,10);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel weightLabel = new JLabel("Weight (kg):");
        weightLabel.setForeground(Color.GREEN);
        c.gridx = 0; c.gridy = 0;
        add(weightLabel,c);
        c.gridx = 1;
        add(weightField,c);

        JLabel heightLabel = new JLabel("Height (cm):");
        heightLabel.setForeground(Color.GREEN);
        c.gridx = 0; c.gridy = 1;
        add(heightLabel,c);
        c.gridx = 1;
        add(heightField,c);

        JButton calcBtn = new JButton("Calculate BMI");
        c.gridx = 0; c.gridy = 2; c.gridwidth = 2;
        add(calcBtn,c);

        resultLabel.setForeground(Color.WHITE);
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        c.gridy = 3;
        add(resultLabel,c);

        calcBtn.addActionListener(e -> calculateBMI());
    }

    private void calculateBMI(){
        try{
            double weight = Double.parseDouble(weightField.getText());
            double heightCm = Double.parseDouble(heightField.getText());
            double heightM = heightCm / 100;
            double bmi = weight / (heightM * heightM);

            String category;
            if(bmi < 18.5) category = "Underweight";
            else if(bmi < 25) category = "Normal";
            else if(bmi < 30) category = "Overweight";
            else category = "Obese";

            resultLabel.setText(String.format("Your BMI: %.2f (%s)", bmi, category));
        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Please enter valid numbers.","Error",JOptionPane.ERROR_MESSAGE);
        }
    }
}
