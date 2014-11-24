/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javagame;

import java.util.*;
/**
 *
 * @author EvanKirkland
 */
public class Person {
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

    /* Getter Functions */
    public int getCondition() {
        return condition;
    }
    public int getAge() {
        return age;
    }

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
