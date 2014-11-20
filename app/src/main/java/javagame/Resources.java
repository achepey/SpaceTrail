/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javagame;

import java.util.*;
import java.io.*;
/**
 *
 * @author EvanKirkland
 */
public class Resources {
    int money, food, fuel, compound;
    ArrayList<Part> spares;
    String race_compound;

    /* Setter Functions */
    public void setMoney(int val) {
        money = val;
    }
    public void setFood(int val) {
        food = val;
    }
    public void setFuel(int val) {
        fuel = val;
    }
    public void setCompound(int val) {
        compound = val;
    }

    /* Getter Functions */
    public int getMoney() {
        return money;
    }
    public int getFood() {
        return food;
    }
    public int getFuel() {
        return fuel;
    }
    public int getCompound() {
        return compound;
    }

    /* Increment Functions */
    /* For bool value, T is ADD, F is SUB */
    public void incrementMoney(int val, boolean action) {
        if(action) {
            money += val;
        } else {
            money -= val;
        }
    }
    public void incrementFood(int val, boolean action) {
        if(action) {
            food += val;
        } else {
            food -= val;
        }
    }
    public void incrementFuel(int val, boolean action) {
        if(action) {
            fuel += val;
        } else {
            fuel -= val;
        }
    }
    public void incrementCompound(int val, boolean action) {
        if(action) {
            compound += val;
        } else {
            compound -= val;
        }
    }
}
