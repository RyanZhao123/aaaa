package com.example.comsci;

import java.util.Date;

public class User {
    private String name;
    private String email;
    private boolean isMale;
    private double weight;
    private double height;
    private Date dateOfBirth;

    public User()
    {
    }

    public User(String name, String email, boolean isMale)
    {
        this.name = name;
        this.email = email;
        this.isMale = isMale;
        //this.dateOfBirth = dateOfBirth;
    }

   /* public int CalorieCalculator(double weight, double height, int age, boolean isMale)
    {
        if(isMale)
        {
            int i = 66.5 + (13.75*weight) + (5.003*height);
        }
    }

    */

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

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
