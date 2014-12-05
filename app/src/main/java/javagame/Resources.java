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
public class Resources implements Serializable{
    private int food, fuel, compound, aluminum;
    private ArrayList<Part> spares;
    private String race_compound;

    public Resources() {
        spares = new ArrayList<Part>();
        compound = 100;
    }

    /* Setter Functions */
    public void setFood(int val) {
        food = val;
    }
    public void setFuel(int val) {
        fuel = val;
    }
    public void setCompound(int val) {
        compound = val;
    }
    public void setAluminum(int val) {
        aluminum = val;
    }
    public void addSpare(Part p) {
        spares.add(p);
    }   //Part name needs to be either engine, wing, or livingBay
    public void addSpareList(ArrayList<Part> partsList) {
        spares = partsList;
    }
    public boolean removeSpare(String partName) {
        for(int i = 0; i < spares.size(); ++i) {
            if(spares.get(i).getName().equals(partName)) {
                spares.remove(i);
                return true;
            }
        }
        return false;
    }

    /* Getter Functions */
    public int getFood() {
        return food;
    }
    public int getFuel() {
        return fuel;
    }
    public int getCompound() {
        return compound;
    }
    public int getAluminum() {
        return aluminum;
    }
    public ArrayList<Part> getSpares() {
        return spares;
    }
    public int getSpareEngines() {
        int numSpares = 0;
        for(int i = 0; i < spares.size(); ++i) {
            if(spares.get(i).getName().equals("engine")) {
                ++numSpares;
            }
        }
        return numSpares;
    }
    public int getSpareWings() {
        int numSpares = 0;
        for(int i = 0; i < spares.size(); ++i) {
            if(spares.get(i).getName().equals("wing")) {
                ++numSpares;
            }
        }
        return numSpares;
    }
    public int getSpareLivingBays() {
        int numSpares = 0;
        for(int i = 0; i < spares.size(); ++i) {
            if(spares.get(i).getName().equals("livingBay")) {
                ++numSpares;
            }
        }
        return numSpares;
    }
    /* Increment Functions */
    /* For bool value, T is ADD, F is SUB */

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
    public void incrementAluminum(int val, boolean action) {
        if(action) {
            aluminum += val;
        } else {
            aluminum -= val;
        }
    }
}
