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
public class Race implements Serializable {
    String name;
    String strength;
    String weakness;

    public Race() {
        Random randomGenerator = new Random();
        long range = (long)4 - (long)1 + 1;
        long fraction = (long)(range * randomGenerator.nextDouble());
        int number = (int)(fraction + 1);
        /* Names courtesy of http://fantasynamegenerators.com/alien-names.php#.VHN6X4vF-So */
        switch (number) {
            case 1: name = "Stryoks";
                strength = "Helium";
                weakness = "Methane";
                break;
            case 2: name = "Eonims";
                strength = "Helium";
                weakness = "Oxygen";
                break;
            case 3: name = "Oev'ysk";
                strength = "Nitrogen";
                weakness = "Hydrogen";
                break;
            case 4: name = "Creix";
                strength = "Nitrogen";
                weakness = "Carbon Dioxide";
                break;
        }
        System.out.println("Name : [" + name + "]");
        System.out.println("Strength : [" + strength + "]");
        System.out.println("Weakness : [" + weakness + "]");
    }

    /* Getter Functions */
    public String getName() {
        return name;
    }
    public String getStrength() {
        return strength;
    }
    public String getWeakness() {
        return weakness;
    }

    /*Setter Methods */
    public void setName(String n) {
        name = n;
    }
    public void setStrength(String s) {
        strength = s;
    }
    public void setWeakness(String w) {
        weakness = w;
    }
}
