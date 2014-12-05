/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javagame;

import java.io.Serializable;
import java.util.*;
/**
 *
 * @author EvanKirkland
 */
public class Person implements Serializable {
    String name;
    Boolean gender;         // True is (M), False is (F)
    int condition, age;
    Race race;

    /* Constructor */
    public Person(String n) {
        name = n;
        condition = 100;

        /* Time to randomly assign age */
        Random randomGenerator = new Random();
        long range = (long)100 - (long)20 + 1;
        long fraction = (long)(range * randomGenerator.nextDouble());
        age = (int)(fraction + 20);
        System.out.println("Crew member [" + name + "] has age of [" + age + "].");
    };

    /* Setter Functions */
    public void setCondition(int c) {
        condition = c;
    }
    public void setAge(int a) {
        age = a;
    }
    public void setRace(Race r) { race = r; }
    public void setGender(Boolean g) { gender = g; }

    /* Getter Functions */
    public String getName() { return name; }
    public Boolean getGender() { return gender; }
    public int getCondition() {
        return condition;
    }
    public int getAge() {
        return age;
    }
    public Race getRace() { return race; }

    /* Increment Functions */
    /* For bool value, T is ADD, F is SUB */
    public void incrementCondition(int val, boolean action) {
        if(action) {
            condition += val;
        } else {
            condition -= val;
        }
        System.out.println("Person [" + name + "]'s health has risen to [" + condition + "].");
        if(condition < 25) {
            System.out.println("WARNING: Person [" + name + "] at critical health [" + condition + "].");
        }
    }
}
