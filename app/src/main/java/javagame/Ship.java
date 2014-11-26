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
    ArrayList<Part> parts = new ArrayList<Part>();

    /* Constructor */
    public Ship() {
        System.out.println("------------------------------------------");
        System.out.println("Constructing ship.");
        this.addPart("Wing", 100);
        this.addPart("Engine", 100);
        this.addPart("Living Bay", 100);
        System.out.println("Ship has been completed.");
        System.out.println("------------------------------------------");
        // test
    };

    /* Modify Functions */
    public void repairPart(int listIndex, int amount) {
        parts.get(listIndex).incrementStatus(amount, true);
    }
    public void addPart(String name, int status) {
        Part p = new Part(name, status);
        parts.add(p);
        System.out.println("Adding part [" + p.name + "] to ship.");
    }
}
