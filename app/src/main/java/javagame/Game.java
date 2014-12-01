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
    private Planet destination, previous;
    private double distanceRemaining,totalDistance, pace;
    private Race race;
    private int money;
    private boolean gameOver;
    private boolean fast, medium, slow;
    public Game() {
// test
        people = new ArrayList<Person>();
        planets = new ArrayList<Planet>();
        fast = false;
        medium = false;
        slow = false;

        /* Create all 9 planets (or are we not using Pluto) */
        Planet Mercury = new Planet("Mercury", ".\\app\\src\\main\\java\\javagame\\planetData.xml");
        Planet Venus = new Planet("Venus", ".\\app\\src\\main\\java\\javagame\\planetData.xml");
        Planet Earth = new Planet("Earth", ".\\app\\src\\main\\java\\javagame\\planetData.xml");
        Planet Mars = new Planet("Mars", ".\\app\\src\\main\\java\\javagame\\planetData.xml");
        Planet Jupiter = new Planet("Jupiter", ".\\app\\src\\main\\java\\javagame\\planetData.xml");
        Planet Saturn = new Planet("Saturn", ".\\app\\src\\main\\java\\javagame\\planetData.xml");
        Planet Uranus = new Planet("Uranus", ".\\app\\src\\main\\java\\javagame\\planetData.xml");
        Planet Neptune = new Planet("Neptune", ".\\app\\src\\main\\java\\javagame\\planetData.xml");
//        Planet Pluto = new Planet("Pluto");

        planets.add(Mercury);
        planets.add(Venus);
        planets.add(Earth);
        planets.add(Mars);
        planets.add(Jupiter);
        planets.add(Saturn);
        planets.add(Uranus);
        planets.add(Neptune);
//        planets.add(Pluto);

        ship = new Ship();
        resources = new Resources();
        destination = new Planet("Temp", "");       //used as default destination until a planet is given
        previous = new Planet("Temp", "");
        race = new Race();
        gameOver = false;
    }

    /* Fuel cost based on distanceRemaining from sun */
    public boolean sellFuel(int m) {
        double cost = destination.fuelCost;
        money = money - (int)(cost * m);
        if(money < 0) {                         // make sure that this would not bankrupt
            return false;
        }
        resources.incrementFuel(m, true);
        return true;
    }

    /* Food cost based on distanceRemaining from sun (medium range is cheapest) */
    public boolean sellFood(int m) {
        double cost = destination.foodCost;
        money = money - (int)(cost * m);
        if(money < 0) {                         // make sure that this would not bankrupt
            return false;
        }
        resources.incrementFood(m, true);
        return true;
    }

    /* Cheap on Earth, Mars, Pluto, and Mercury */
    public boolean sellAluminum(int m) {
        double cost = destination.aluminumCost;
        money = money - (int)(cost * m);
        if(money < 0) {                         // make sure that this would not bankrupt
            return false;
        }
        resources.incrementAluminum(m, true);
        return true;
    }

    /* Cheap on Earth, Mars, Pluto, and Mercury */
    public boolean sellParts(int m, String s) {
        double cost = destination.partCost;
        money = money - (int)(cost * m);
        if(money < 0) {                         // make sure that this would not bankrupt
            return false;
        }
        for (int i = 0; i < m; i++) {           // add 'm' of this spare part
            if(s.equals("Engine")) {
                Part p = new Part("Engine", 100);
                resources.addSpare(p);
            }else if(s.equals("Wing")) {
                Part p = new Part("Wing", 100);
                resources.addSpare(p);
            }else if(s.equals("LivingBay")) {
                Part p = new Part("LivingBay", 100);
                resources.addSpare(p);
            }
        }
        return true;
    }

    /* Makes moves towards planet destination */
    public boolean makeMove() {
        if(gameOver || isWinner()) {
            return false;
        }
        /* Things that need to be done in this method:
            - reduce distanceRemaining remaining
            - decide how much health each crew member loses
            - decide how to measure the pace (how many taps on the phone does it take?)
         */
        distanceRemaining -= pace;                       // reduce distanceRemaining remaining
        crewAttrition();                        // decide how much health each crew member loses
        resourceAttrition();                    // decide how many resources the crew loses
        String issue = getIssue();
        if(!issue.equals("Successful Movement")){
            System.out.println(issue);
        }
        if(gameOver) {
            System.out.println("Game Over! You lose!");
        }
        return distanceRemaining <= 0;
    }

    public void resourceAttrition() {
        if(fast) {
            resources.incrementFuel(10, false);
            resources.incrementFood(10, false);
            resources.incrementCompound(10, false);
            ship.damagePart("engine", 10);
            ship.damagePart("wing", 10);
            ship.damagePart("livingBay", 10);
        }else if(medium) {
            resources.incrementFuel(8, false);
            resources.incrementFood(8, false);
            resources.incrementCompound(8, false);
            ship.damagePart("engine", 8);
            ship.damagePart("wing", 8);
            ship.damagePart("livingBay", 8);
        }else if(slow) {
            resources.incrementFuel(6, false);
            resources.incrementFood(6, false);
            resources.incrementCompound(6, false);
            ship.damagePart("engine", 6);
            ship.damagePart("wing", 6);
            ship.damagePart("livingBay", 6);
        }
    }

    public void crewAttrition() {
        /* Attrition will be measured based on destination planet
            - will be based on the compounds of the destination planet and race of the crew
            - will be different based on crew member (age, current condition)
        */
        for(int i = 0; i < 5; i++) {
            int amount = 0;                 // How much the crew member will be hurt
            /* Measure attrition due to race */
            if(people.get(i).getRace().getStrength().equals(destination.compound1) || people.get(i).getRace().getStrength().equals(destination.compound2)) {
                amount += 10;               // Add health for heading to planet with strength compound
            }
            if(people.get(i).getRace().getWeakness().equals(destination.compound1) || people.get(i).getRace().getWeakness().equals(destination.compound2)) {
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
            people.get(0).setRace(race);
        }
        else {
            people.add(new Person(name));
            System.out.println("Crew member [" + name + "] has been added.");
            people.get(people.size()-1).setRace(race);
        }
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public Person getCrew(int personIndex) {
        return people.get(personIndex);
    }

    public void setFirstDestination(int planetIndex) {
        destination = planets.get(planetIndex);
        distanceRemaining = destination.distanceFromSun;
        totalDistance = destination.distanceFromSun;
    }

    public void setDestination(String planet) {
        if(planet.equals("Mercury")) {
            setDestination(0);
        }
        else if(planet.equals("Venus")) {
            setDestination(1);
        }
        else if(planet.equals("Earth")) {
            setDestination(2);
        }
        else if(planet.equals("Mars")) {
            setDestination(3);
        }
        else if(planet.equals("Jupiter")) {
            setDestination(4);
        }
        else if(planet.equals("Saturn")) {
            setDestination(5);
        }
        else if(planet.equals("Uranus")) {
            setDestination(6);
        }
        else if(planet.equals("Neptune")) {
            setDestination(7);
        }
    /*    else if(planet.equals("Pluto")) {
            setDestination(8);
        }*/
    }

    /*Will set the destination to the correct planet (MUST HAVE A DESTINATION PREVIOUSLY)
    @param int planetIndex is the number of the planet in the solar system
     */
    public void setDestination(int planetIndex) {
        previous = destination;
        int currentIndex = 0;
        for(int i = 0; i < 9; i++) {
            if(planets.get(i).name.equals(destination.name)) {
                currentIndex = i;
                break;
            }
        }

        /* Calculating the distanceRemaining between planets
            - each planet has 40 degrees between it and the next one
            - use planet index to compute total degrees between one planet and another
            - find sine value, and apply to destination planet's distanceRemaining
            */
        double hypotenuse = Math.sin(Math.toRadians(40 * (planetIndex - currentIndex)));
        destination = planets.get(planetIndex);
        distanceRemaining = (int)((destination.distanceFromSun) * hypotenuse);
        totalDistance = distanceRemaining;

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

    public void setDistanceRemaining(int d) {
        distanceRemaining = d;
    }

    public double getDistanceRemaining() {
        return distanceRemaining;
    }

    public boolean isWinner() {
        int counter = 0;
        for(int i = 0; i < 8; i++) {
            if (planets.get(i).visited) {
                counter += 1;
            }
        }
        if(counter == 8) {
            System.out.println("You have visited all the planets.");
            return true;
        }
        return false;
    }

    public boolean isLoser() {
        return gameOver;
    }

    public void setMoney(int m) {
        money = m;
    }

    public int getMoney() {
        return money;
    }

    private String getIssue() {
        String issue = "Successful movement!";

        //resource and ship issues
        boolean resourceIssue = false;
        if (resources.getFood() <= 0) {
            issue = "You are out of food! Your crew has resorted to cannibalism and your last member is currently starving to death.";
            gameOver = true;
            resourceIssue = true;
        } else if (resources.getFuel() <= 0) {
            issue = "You are out of fuel! Your ship is currently floating around space somewhere near Saturn's 34th moon.";
            gameOver = true;
            resourceIssue = true;
        } else if (resources.getCompound() <= 0) {
            issue = "You have run out of " + race.getStrength() + "! Your crew can no longer breath and is currently asphyxiating as you read this.";
            gameOver = true;
            resourceIssue = true;
        } else if (ship.getHullStatus() <= 0) {
            issue = "Your hull has sustained critical damage! The structural integrity of the hull is so week that the ship breaks apart when the captain tries to decelerate.";
            gameOver = true;
            resourceIssue = true;
        }
        else if (ship.getEngineStatus() <= 0) {
            issue = "Your engine has sustained critical damage! Your engines explode and the ship goes spiraling into a nearby moon.";
            gameOver = true;
            resourceIssue = true;
        }
        else if (ship.getLivingBayStatus() <= 0) {
            issue = "Your living bay has sustained critical damage! Your crew has been sucked into space through a hole in space ship.";
            gameOver = true;
            resourceIssue = true;
        }
        else if (ship.getWingStatus() <= 0) {
            issue = "Your wing has sustained critical damage! The ship can no longer be controlled and your crew escapes in an escape pod, except the captain who goes down with the ship.";
            gameOver = true;
            resourceIssue = true;
        }
        if (resourceIssue) {
            return issue;
        }
        //health issues
        for (int i = 0; i < people.size(); ++i) {
            if (people.get(i).getCondition() <= 0) {
                if (i == 0) {
                    issue = "Your captain has died! Unfortunately, he did not name a second-in-command and the entire crew is killed in the resulting power struggle.";
                    if(people.size() == 1) {
                        issue = "Your captain has died! He was the last living crew member for you spaceship! Without anyone at the controls, the ship eventually crashes on Venus and is destroyed.";
                    }
                    gameOver = true;
                    return issue;
                } else {
                    double messageChance = Math.random();
                    if(messageChance > .75)
                        issue = people.get(i).getName() + " has died! You hold a moving ceremony for your lost crew member, but slowly forget about them.";
                    else if(messageChance > .5)
                        issue = people.get(i).getName() + " has died! " + people.get(i).getName() + " was the captain's favorite crew member, so " + people.get(0).getName() + " is feeling pretty down.";
                    else if(messageChance > .25) {
                        issue = people.get(i).getName() + " has died! " + people.get(i).getName() + " was disliked by the rest of the crew, so morale is higher than ever right now.";
                    }
                    else if(messageChance > 0) {
                        issue = people.get(i).getName() + " has died! " + people.get(i).getName() + " was the best cook on the spaceship, so the crew is really going to miss him around dinner time";
                    }
                    people.remove(i);
                    --i;
                    return issue;
                }
            }
        }
        //previous planet dependent issues
            //will encounter previous planet issues if within close distanceRemaining to previously visited planet
        if(totalDistance == distanceRemaining) {
            double escapeVelocityIssueChance = Math.random();
            if(escapeVelocityIssueChance < previous.escapeVelocity/300) {
                issue = "While leaving " + previous.name + " you did not correctly accommodate for the planet's escape velocity, so you used more fuel than was initially planned for to leave the atmosphere!";
                resources.incrementFuel(10, false);
                return issue;
            }
            double temperatureIssueChance = Math.random();
            if(Math.abs(previous.meanTemperature) > 150 && temperatureIssueChance < .05) {      //5% chance
                issue = "While on " + previous.name + " your supplies were damaged by the extreme temperatures! You have to throw away some of your food as it is now not edible.";
                resources.incrementFood(10, false);
                return issue;
            }
            else if(Math.abs(previous.meanTemperature) > 150 && temperatureIssueChance < .07) {      //2% chance
                int crewMember = (int)Math.floor(Math.random() * people.size());
                issue = "While on " + previous.name + ", " + people.get(crewMember).getName() + " got sick from the extreme temperatures! " + people.get(crewMember).getName() +"'s health has lowered.";
                people.get(crewMember).incrementCondition(10, false);
                return issue;
            }
        }
        //destination dependent issues
           //will encounter destination issues if within close distanceRemaining to destination planet
        if((distanceRemaining < pace*2 && slow) || (distanceRemaining < pace && (medium || fast))) {
            double moonIssueChance = Math.random();
            double ringIssueChance = (destination.ringSystem) ? Math.random() : 0;
            if(moonIssueChance < destination.numberOfMoons/200) {       //higher chance for planets with more moons
                issue = "While approaching " + destination.name + " you miscalculated one of its moon's orbits. The trip takes longer and you use more resources than expected.";
                crewAttrition();                        // decide how much health each crew member loses
                resourceAttrition();                    // decide how many resources the crew loses
                return issue;
            }
            if(ringIssueChance < .05) {
                double shipPartChance = Math.random();
                String shipPart;
                if(shipPartChance > .5) {
                    shipPart = "hull";
                }
                else if(shipPartChance > .34) {
                    shipPart = "engine";
                }
                else if(shipPartChance > .18) {
                    shipPart = "wing";
                }
                else {
                    shipPart = "livingBay";
                }
                ship.damagePart(shipPart, 5);
                issue = "You have traveled too close to " + destination.name + "'s rings! Your " + shipPart + " has been damaged by a large chunk of ice!";
                return issue;
            }
        }
        if(distanceRemaining <= 0) {     //If you arrived at the planet this turn
            double nightOrDayChance = Math.random();
            if(nightOrDayChance > .5) {         //you have arrived at night time to the planet
                if(destination.lengthOfDay > 30) {      //It would take too long to wait for it to become day
                    issue = "After landing on " + destination.name + " at night, you realize that the length of the planet's night is " + destination.lengthOfDay/2 + " hours. You fly around the planet to land where it is daytime.";
                    crewAttrition();                        // decide how much health each crew member loses
                    resourceAttrition();                    // decide how many resources the crew loses
                    return issue;
                }
            }
            double diameterIssueChance = Math.random();
            if(diameterIssueChance < destination.diameter/1000000) {        //diameter will be a decimal if divided by 1000000
                issue = "The analysis probe you sent to measure the planet's size appears to have gotten lost! You have to buy a new one.";
                money -= 20;
                return issue;
            }
            double gravityIssueChance = Math.random();
            if(gravityIssueChance < .04 && (destination.gravity > 10 || destination.gravity < 8)) {
                int crewMember = (int)Math.floor(Math.random() * people.size());
                issue = people.get(crewMember).getName() + " was not expecting the gravity on " + destination.name + " to be so different than the spaceships's artificial gravity and " + people.get(crewMember).getName() + " has hurt their leg.";
                people.get(crewMember).incrementCondition(10, false);
                return issue;
            }
            double cryoSleepIssueChance = Math.random();
            if(totalDistance > 500 && cryoSleepIssueChance < .02) {     //crew only enters cryosleep if distance traveled is over 500 units
                int crewMember = (int)Math.floor(Math.random() * people.size());
                issue = people.get(crewMember).getName() + " is experiencing mild stomach discomfort after awakening from cyrosleep. Totally normal, no need for concern. Their condition is a little lower now, though.";
                people.get(crewMember).incrementCondition(5, false);
                return issue;
            }
            double gettingLostIssueChance = Math.random();
            if(destination.axialTilt > 90 && gettingLostIssueChance < .025) {       //if planet's axis is very tilted
                issue = "Because of " + destination.name + "'s severe axial tilt (" + destination.axialTilt + " degrees) your crew has trouble reading their maps and gets lost. Your crew eats extra food when they are lost because they are nervous-eaters.";
                resources.incrementFood(10, false);
                return issue;
            }
        }

        //travel issues
           //traveling through asteroid belt
        if ((planets.indexOf(destination) < 4 && planets.indexOf(previous) > 4) || planets.indexOf(destination) > 4 && planets.indexOf(previous) < 4) {
            double asteroidChance = Math.random();
            if((slow && asteroidChance < .01) || (medium && asteroidChance < .015) || (fast && asteroidChance < .03)) {
                double shipPartChance = Math.random();
                String shipPart;
                if(shipPartChance > .5) {
                    shipPart = "hull";
                }
                else if(shipPartChance > .34) {
                    shipPart = "engine";
                }
                else if(shipPartChance > .18) {
                    shipPart = "wing";
                }
                else {
                    shipPart = "livingBay";
                }
                issue = "While traveling through the asteroid belt, you have collided with an asteroid and your " + shipPart + " was damaged!";
                ship.damagePart(shipPart, 5);
                return issue;
            }
            //if traveling more than 500 units, the crew will enter cryosleep when traveling
            if(totalDistance > 500) {
                double cryosleepIssueChance = Math.random();
                if(cryosleepIssueChance > .0001) {
                    int crewMember = (int)Math.floor(Math.random() * people.size());
                    issue = people.get(crewMember).getName() + " has been killed due to complications with cryosleep. It was a painless way to go, but I am sure the rest of the crew will be sad to hear it when they wake up.";
                    people.remove(crewMember);
                    return issue;
                }
            }
        }

        //miscellaneous issues
            //random issues that can occur
        double randomIssueChance = Math.random();
        if(randomIssueChance > .01) {
            double shipPartChance = Math.random();
            String shipPart;
            if(shipPartChance > .5) {
                shipPart = "hull";
            }
            else if(shipPartChance > .34) {
                shipPart = "engine";
            }
            else if(shipPartChance > .18) {
                shipPart = "wing";
            }
            else {
                shipPart = "livingBay";
            }
            issue = "You have encountered space pirates who want to steal your valuable aluminum! You escape them but your " + shipPart + " has taken damage!";
            ship.damagePart(shipPart, 5);
            return issue;
        }

        return issue;
    }

    public void setPace(int p) {
        switch(p) {
            case 1:             // 'fast' is 12 taps
                fast = true;
                medium = false;
                slow = false;
                pace = totalDistance/12;
                break;
            case 2:             // 'medium' is 14 taps
                fast = false;
                medium = true;
                slow = false;
                pace = totalDistance/14;
                break;
            case 3:             // 'slow' is 16 taps
                fast = false;
                medium = false;
                slow = true;
                pace = totalDistance/16;
                break;
        }
    }
}