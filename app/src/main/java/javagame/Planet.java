/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javagame;

/**
 *
 * @author EvanKirkland
 */
public class Planet {
    String name, compound1, compound2;
    int distance;

    /* Constructor */
    public Planet(String n, String c1, String c2, int d) {
        name = n;
        compound1 = c1;
        compound2 = c2;
        distance = d;
    };
}