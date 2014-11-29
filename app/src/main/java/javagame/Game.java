/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javagame;

import java.util.*;
import java.io.*;
import java.lang.Math;
/**
 *
 * @author EvanKirkland
 */
public class Game implements Serializable {
    private Ship ship;
    private Resources resources;
    private ArrayList<Person> people;
    private ArrayList<Planet> planets;
    private Planet destination;
    private int distance;
    private Race race;
    public Game() {

        people = new ArrayList<Person>();
        planets = new ArrayList<Planet>();

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

        ship = new Ship();
        resources = new Resources();
        destination = new Planet("Temp", "Temp", "Temp", -1);       //used as default destination until a planet is given

    }

    /* Makes moves towards planet destination */
    public boolean makeMove() {
        /* Things that need to be done in this method:
            - reduce distance remaining
            - decide how much health each crew member loses
            - decide how to measure the pace (how many taps on the phone does it take?)
         */
        distance -= 10;                     // reduce distance remaining
        crewAttrition();                    // decide how much health each crew member loses
        return distance <= 0;
    }

    public void crewAttrition() {
        /* Attrition will be measured based on destination planet
            - will be based on the compounds of the destination planet and race of the crew
            - will be different based on crew member (age, current condition)
        */
        for(int i = 0; i < 5; i++) {
            int amount = 0;                 // How much the crew member will be hurt
            /* Measure attrition due to race */
            if(people.get(i).getRace().getStrength().equals(destination.getCompound1()) || people.get(i).getRace().getStrength().equals(destination.getCompound2())) {
                amount += 5;               // Add health for heading to planet with strength compound
            }
            if(people.get(i).getRace().getWeakness().equals(destination.getCompound1()) || people.get(i).getRace().getWeakness().equals(destination.getCompound2())) {
                amount -= 10;               // Subtract health for heading to planet with weakness compound
            }

            /* Measure attrition due to age */
            if(people.get(i).getAge() > 75) {    // THEY GON' DIE...but not really, they will just suffer more
                amount -= 5;
            }

            /* Apply the subtraction/addition to the crew status */
            System.out.println("Crew member [" + people.get(i).getName() + "] has had their health changed by [" + amount + "].");
            people.get(i).incrementCondition(amount, true);
        }
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

    public Person getCrew(int personIndex) {
        return people.get(personIndex);
    }

    public void setFirstDestination(int planetIndex) {
        destination = planets.get(planetIndex);
    }

    public void setDestination(String planet) {
        if(planet.equals("Mercury")) {
            setDestination(1);
        }
        else if(planet.equals("Venus")) {
            setDestination(2);
        }
        else if(planet.equals("Earth")) {
            setDestination(3);
        }
        else if(planet.equals("Mars")) {
            setDestination(4);
        }
        else if(planet.equals("Jupiter")) {
            setDestination(5);
        }
        else if(planet.equals("Saturn")) {
            setDestination(6);
        }
        else if(planet.equals("Uranus")) {
            setDestination(7);
        }
        else if(planet.equals("Neptune")) {
            setDestination(8);
        }
        else if(planet.equals("Pluto")) {
            setDestination(9);
        }
    }

    /*Will set the destination to the correct planet (MUST HAVE A DESTINATION PREVIOUSLY)
    @param int planetIndex is the number of the planet in the solar system
     */
    public void setDestination(int planetIndex) {
        int currentIndex = 0;
        for(int i = 0; i < 9; i++) {
            if(planets.get(i).getName() == destination.getName()) {
                currentIndex = i;
                break;
            }
        }

        /* Calculating the distance between planets
            - each planet has 40 degrees between it and the next one
            - use planet index to compute total degrees between one planet and another
            - find sine value, and apply to destination planet's distance
            */
        double hypotenuse = Math.sin(Math.toRadians(40 * (planetIndex - currentIndex)));
        destination = planets.get(planetIndex);
        distance = (int)((destination.getDistance()) * hypotenuse);

    }

    /*Returns destination planet */
    public Planet getDestination() {
        return destination;
    }

    /* Will set the races of all the crew members
    @param Race r is the randomly generated race that will be applied to the crew
     */
    public void setCrewRace(Race r) {
        for(int i = 0; i < 5; i++) {
            people.get(i).setRace(r);
        }
    }

    public void setDistance(int d) {
        distance = d;
    }

    public int getDistance() {
        return distance;
    }
}