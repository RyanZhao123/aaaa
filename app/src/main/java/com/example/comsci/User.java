package com.example.comsci;

import java.time.Period;
import java.util.Date;
import java.time.LocalDate;

public class User {
    private String name;
    private String email; //need to figure out why email isn't accessed
    private boolean isMale;
    private int weight;
    private int height;
    private Date dateOfBirth;
    private int calorieGoal;

    public User()
    {
    }

    public User(String name, String email)
    {
        this.name = name;
        this.email = email;
        //this.dateOfBirth = dateOfBirth;
    }

   /*


   public int calculateAge(LocalDate birthDate, LocalDate currentDate) {
       if ((birthDate != null) && (currentDate != null)) {
           return Period.between(birthDate, currentDate).getYears();
       } else {
           return 0;
       }
   }
   */

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCalorieGoal() {
        return calorieGoal;
    }

    public void setCalorieGoal(int calorieGoal) {
        this.calorieGoal = calorieGoal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
