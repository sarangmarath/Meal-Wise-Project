# MealWise - Java Swing Meal Planner

MealWise is a Java Swing desktop application to manage your meals, track calories, plan weekly meals, calculate BMI, and manage budget. It allows users to create recipes, plan meals for each day, view a calendar of planned meals, and export the calendar to PDF.

---

## Features

- **Recipe Manager:** Add, edit, delete, and filter recipes by dietary type (None, Vegetarian, Vegan, High-Protein).  
- **Meal Planner:** Assign meals to each day of the week and calculate total calories.  
- **Calendar:** Visual overview of your weekly meal plan.  
- **PDF Export:** Export your weekly meal plan to a PDF.  
- **BMI Calculator:** Calculate your Body Mass Index and check your health category.  
- **Budget Planner:** Track daily meal costs and total weekly budget.  

---

## Requirements

- Java 8 or higher  
- MySQL (for storing users and recipes)  
- MySQL Connector (included in `lib/mysql-connector-j-9.4.0.jar`)  

---

## How it Works

1. **Login/Register:** Create an account or log in with existing credentials.  
2. **Recipe Management:** Add your favorite recipes with ingredients, calories, and dietary type.  
3. **Meal Planning:** Assign meals to each day using the Meal Planner panel.  
4. **Calendar Overview:** Check your weekly plan and total calories per day.  
5. **Export PDF:** Export the calendar to PDF directly from the app.  
6. **BMI & Budget:** Use the BMI Calculator and Budget Planner to track health and finances.  

---

## How to Run

1. Clone the repository:

   ```bash
   git clone https://github.com/sarangmarath/Meal-Wise-Project.git


## Collaboration / Contributors

This project was built in collaboration by the following contributors:

- **sarangmarath** - [GitHub](https://github.com/sarangmarath)  
- **Kesav-Mangalathil** - [GitHub](https://github.com/Kesav-Mangalathil)  
- **Chandu-2908** - [GitHub](https://github.com/Chandu-2908)  
- **rayyandeys** - [GitHub](https://github.com/rayyandeys)

## Notes 

- The app uses a local MySQL database to store user accounts and recipes. Make sure MySQL is installed and running.  
- The MySQL Connector JAR is included in the `lib` folder for JDBC connectivity.  
- PDF export works without external libraries, so you don’t need to install additional JARs for PDF generation.  
- This project was developed using Java Swing, focusing on desktop GUI applications.  
- Make sure to configure your MySQL database credentials in `Database.java` before running the app.  
- All recipe, meal plan, and calendar data are persisted locally in the database.  

