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
public class Game implements Serializable {
    Ship ship;
    Resources resources;
    ArrayList<Person> people = new ArrayList<Person>();
    ArrayList<Planet> planets = new ArrayList<Planet>();
    Planet destination;
    Race race;
    public Game() {
        Scanner s = new Scanner(System.in);
        /* Create all 9 planets (or are we not using Pluto) */
        Planet Mercury = new Planet("Mercury", "Helium", "Oxygen", 100);
        Planet Venus = new Planet("Venus", "Nitrogen", "Carbon Dioxide", 100);
        Planet Earth = new Planet("Earth", "Nitrogen", "Oxygen", 100);
        Planet Mars = new Planet("Mars", "Nitrogen", "Carbon Dioxide", 100);
        Planet Jupiter = new Planet("Jupiter", "Helium", "Hydrogen", 100);
        Planet Saturn = new Planet("Saturn", "Helium", "Hydrogen", 100);
        Planet Uranus = new Planet("Uranus", "Helium", "Methane", 100);
        Planet Neptune = new Planet("Neptune", "Helium", "Methane", 100);
        Planet Pluto = new Planet("Pluto", "Nitrogen", "Methane", 100);

        planets.add(Mercury);
        planets.add(Venus);
        planets.add(Earth);
        planets.add(Mars);
        planets.add(Jupiter);
        planets.add(Saturn);
        planets.add(Uranus);
        planets.add(Neptune);
        planets.add(Pluto);

    }
    /* Display between turns, will eventually be called by pressing and holding on screen of phone */
    public void printInfo() {

    }

    /* Changes variables for purchasing supplies */
    public void changeInfo() {

    }

    /* Makes moves towards planet destination */
    public void makeMove() {

    }

    /*Returns the resources object (Daniel)*/
    public Resources getResources() {
        return resources;
    }

    /*Returns the ship object (Daniel)*/
    public Ship getShip() {
        return ship;
    }

    /*Will add a crew member to the ship (Daniel)
     @param String name the name of the crew member
     @param boolean captain true if this crew member is to be the ship captain
     */
    public void addCrew(String name, boolean captain) {
        if(captain) {
            people.add(0, new Person(name));
            System.out.println("Captain [" + name + "] has been added.");
        }
        else {
            people.add(new Person(name));
            System.out.println("Crew member [" + name + "] has been added.");
        }
    }
    /*Will set the destination to the correct planet
    @param int planetIndex is the number of the planet in the solar system
     */
    public void setDestination(int planetIndex) {
        destination = planets.get(planetIndex);
    }

    /* Will set the races of all the crew members
    @param Race r is the randomly generated race that will be applied to the crew
     */
    public void setCrewRace(Race r) {
        for(int i = 0; i < 4; i++) {
            people.get(i).setRace(r);
        }
    }
}