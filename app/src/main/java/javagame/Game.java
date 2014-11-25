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
public class Game {
    Ship ship;
    Resources resources;
    ArrayList<Person> people = new ArrayList<Person>();
    Planet destination;
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

        /* Declare captain of ship */
        System.out.print("Welcome to Space Trail. What would you like your captain's name to be?\n: ");
        String captain = s.nextLine();
        Person capt = new Person(captain);
        people.add(capt);

        /* Add the crew (4) */
        System.out.print("Please enter name for [Crew Member 1] : ");
        String crew1 = s.nextLine();
        Person c1 = new Person(crew1);
        people.add(c1);
        System.out.print("Please enter name for [Crew Member 2] : ");
        String crew2 = s.nextLine();
        Person c2 = new Person(crew2);
        people.add(c2);
        System.out.print("Please enter name for [Crew Member 3] : ");
        String crew3 = s.nextLine();
        Person c3 = new Person(crew3);
        people.add(c3);
        System.out.print("Please enter name for [Crew Member 4] : ");
        String crew4 = s.nextLine();
        Person c4 = new Person(crew4);
        people.add(c4);

        /* Create the ship, with captain (1) */
        ship = new Ship(captain);

        /* Declare race of crew */
        System.out.println("Cpt. " + captain + " and the crew are members of the following race: ");
        Race race = new Race();
        c1.race = race;
        c2.race = race;
        c3.race = race;
        c4.race = race;
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
        }
        else {
            people.add(new Person(name));
        }
    }
}