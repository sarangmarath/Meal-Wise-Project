import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.util.List;

public class RecipeManagerPanel extends JPanel {

    public JComboBox<String> filterBox = new JComboBox<>(new String[]{"All", "None", "Vegetarian", "Vegan", "High-Protein"});
    private DefaultListModel<Recipe> recipeListModel = new DefaultListModel<>();
    private JList<Recipe> recipeList = new JList<>(recipeListModel);

    private JTextField nameField = new JTextField(15);
    private JTextField ingredientsField = new JTextField(20);
    private JTextField caloriesField = new JTextField(5);
    private JComboBox<String> dietaryBox = new JComboBox<>(new String[]{"None", "Vegetarian", "Vegan", "High-Protein"});

    public RecipeManagerPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(34,34,34));

        // Top filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(new Color(34,34,34));
        JLabel filterLabel = new JLabel("Filter by Dietary Type:");
        filterLabel.setForeground(new Color(180,180,180));
        filterPanel.add(filterLabel);
        filterPanel.add(filterBox);
        add(filterPanel, BorderLayout.NORTH);

        // Recipe list
        recipeList.setCellRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus){
                JLabel label = (JLabel) super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
                if(value instanceof Recipe){
                    Recipe r = (Recipe) value;
                    label.setText(r.getName() + " (" + r.getDietaryType() + ", " + r.getCalories() + " cal)");
                    label.setForeground(Color.GREEN);
                }
                return label;
            }
        });
        recipeList.setBackground(new Color(44,44,44));
        recipeList.setForeground(new Color(180,180,180));
        JScrollPane scrollPane = new JScrollPane(recipeList);
        scrollPane.setPreferredSize(new Dimension(300,400));

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5,2,5,5));
        formPanel.setBackground(new Color(34,34,34));
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Ingredients (comma separated):"));
        formPanel.add(ingredientsField);
        formPanel.add(new JLabel("Calories:"));
        formPanel.add(caloriesField);
        formPanel.add(new JLabel("Dietary Type:"));
        formPanel.add(dietaryBox);

        // Buttons
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(34,34,34));
        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(34,34,34));
        rightPanel.add(formPanel, BorderLayout.CENTER);
        rightPanel.add(btnPanel, BorderLayout.SOUTH);

        add(scrollPane, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        // Initial load
        loadRecipesFromDB("All");

        // Filter action
        filterBox.addActionListener(e -> applyFilter());

        // List selection
        recipeList.addListSelectionListener(e -> {
            int idx = recipeList.getSelectedIndex();
            if(idx>=0){
                Recipe r = recipeListModel.get(idx);
                nameField.setText(r.getName());
                ingredientsField.setText(String.join(",",r.getIngredients()));
                caloriesField.setText(String.valueOf(r.getCalories()));
                dietaryBox.setSelectedItem(r.getDietaryType());
            }
        });

        // Add recipe
        addBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String ing = ingredientsField.getText().trim();
            String calStr = caloriesField.getText().trim();
            String type = (String) dietaryBox.getSelectedItem();
            if(!name.isEmpty() && !ing.isEmpty() && !calStr.isEmpty()){
                try{
                    int cal = Integer.parseInt(calStr);
                    Recipe r = new Recipe(0,name,Arrays.asList(ing.split(",")),cal,type);
                    insertRecipeToDB(r);
                    applyFilter(); // reload filtered list
                    clearFields();
                }catch(NumberFormatException ex){
                    JOptionPane.showMessageDialog(this,"Calories must be a number.");
                }
            }
        });

        // Edit recipe
        editBtn.addActionListener(e -> {
            int idx = recipeList.getSelectedIndex();
            if(idx>=0){
                Recipe r = recipeListModel.get(idx);
                r.setName(nameField.getText().trim());
                r.setIngredients(Arrays.asList(ingredientsField.getText().trim().split(",")));
                try{
                    r.setCalories(Integer.parseInt(caloriesField.getText().trim()));
                }catch(NumberFormatException ex){
                    JOptionPane.showMessageDialog(this,"Calories must be a number.");
                    return;
                }
                r.setDietaryType((String) dietaryBox.getSelectedItem());
                updateRecipeInDB(r);
                applyFilter(); // reload filtered list
            }
        });

        // Delete recipe
        deleteBtn.addActionListener(e -> {
            int idx = recipeList.getSelectedIndex();
            if(idx>=0){
                Recipe r = recipeListModel.get(idx);
                deleteRecipeFromDB(r.getId());
                applyFilter(); // reload filtered list
                clearFields();
            }
        });
    }

    private void clearFields(){
        nameField.setText("");
        ingredientsField.setText("");
        caloriesField.setText("");
        dietaryBox.setSelectedIndex(0);
    }

    private void applyFilter(){
        String filter = (String) filterBox.getSelectedItem();
        loadRecipesFromDB(filter);
    }

    // ------------------ Database Methods ------------------
    private void loadRecipesFromDB(){ loadRecipesFromDB("All"); }

    private void loadRecipesFromDB(String filter){
        recipeListModel.clear();
        try(Connection conn = Database.getConnection()){
            String sql = "SELECT id,name,ingredients,calories,dietary_type FROM recipes";
            if(!"All".equals(filter)){
                sql += " WHERE dietary_type=?";
            }
            PreparedStatement stmt = conn.prepareStatement(sql);
            if(!"All".equals(filter)){
                stmt.setString(1,filter);
            }
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                List<String> ingredients = Arrays.asList(rs.getString("ingredients").split(","));
                int cal = rs.getInt("calories");
                String type = rs.getString("dietary_type");
                recipeListModel.addElement(new Recipe(id,name,ingredients,cal,type));
            }
        }catch(Exception ex){ ex.printStackTrace(); }
    }

    private void insertRecipeToDB(Recipe r){
        try(Connection conn = Database.getConnection()){
            String sql = "INSERT INTO recipes(name,ingredients,calories,dietary_type) VALUES(?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,r.getName());
            stmt.setString(2,String.join(",",r.getIngredients()));
            stmt.setInt(3,r.getCalories());
            stmt.setString(4,r.getDietaryType());
            stmt.executeUpdate();
        }catch(Exception ex){ ex.printStackTrace(); }
    }

    private void updateRecipeInDB(Recipe r){
        try(Connection conn = Database.getConnection()){
            String sql = "UPDATE recipes SET name=?, ingredients=?, calories=?, dietary_type=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,r.getName());
            stmt.setString(2,String.join(",",r.getIngredients()));
            stmt.setInt(3,r.getCalories());
            stmt.setString(4,r.getDietaryType());
            stmt.setInt(5,r.getId());
            stmt.executeUpdate();
        }catch(Exception ex){ ex.printStackTrace(); }
    }

    private void deleteRecipeFromDB(int id){
        try(Connection conn = Database.getConnection()){
            String sql = "DELETE FROM recipes WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1,id);
            stmt.executeUpdate();
        }catch(Exception ex){ ex.printStackTrace(); }
    }

    // ------------------ Getters ------------------
    public List<Recipe> getFilteredRecipeList(){
        List<Recipe> list = new ArrayList<>();
        for(int i=0;i<recipeListModel.size();i++){
            list.add(recipeListModel.get(i));
        }
        return list;
    }
}
