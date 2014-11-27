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
public class Ship {
    private int engine;
    private int wing;
    private int livingBay;

    /* Constructor */
    public Ship() {
        System.out.println("------------------------------------------");
        System.out.println("Constructing ship.");
        engine = 100;
        wing = 100;
        livingBay = 100;
        System.out.println("Ship has been completed.");
        System.out.println("------------------------------------------");
        // test
    };

    /* Modify Functions */
    public void repairPart(String partName, int amount) {
        if(partName.equals("engine")){
            engine += amount;
            if(engine > 100) {
                engine = 100;
            }
        }
        else if(partName.equals("wing")){
            wing += amount;
            if(wing > 100) {
                wing = 100;
            }
        }
        else if(partName.equals("livingBay")){
            livingBay += amount;
            if(livingBay > 100) {
                livingBay = 100;
            }
        }
    }

    //Getter methods
    public int getEngineStatus() {
        return engine;
    }

    public int getWingStatus() {
        return wing;
    }

    public int getLivingBayStatus() {
        return livingBay;
    }
}
