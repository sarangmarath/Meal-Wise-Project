// =============================
// Recipe.java
// =============================
import java.util.List;

public class Recipe {
    private int id;
    private String name;
    private List<String> ingredients;
    private int calories;
    private String dietaryType;

    public Recipe(int id, String name, List<String> ingredients, int calories, String dietaryType) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.calories = calories;
        this.dietaryType = dietaryType;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public List<String> getIngredients() { return ingredients; }
    public int getCalories() { return calories; }
    public String getDietaryType() { return dietaryType; }
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }
    public void setCalories(int calories) { this.calories = calories; }
    public void setDietaryType(String dietaryType) { this.dietaryType = dietaryType; }

    @Override
    public String toString() {
        return name + " (" + dietaryType + ", " + calories + " cal)";
    }
}
