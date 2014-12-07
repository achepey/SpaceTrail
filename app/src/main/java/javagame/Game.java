/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javagame;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;
import java.lang.Math;
/**
 *
 * @author Evan Kirkland and Robert Christian
 */
public class Game implements Serializable {

    private Ship ship;      //The player's spaceship
    private Resources resources;        //The player's resources
    private ArrayList<Person> people;       //The crew of the spaceship - the captain is always at index 0, and there is a max size of 5
    private ArrayList<Planet> planets;      //The planets in the solar system
    private Planet destination, previous;       //The current destination and the previous planet traveled to
    private double distanceRemaining,totalDistance, pace;       //how much distance is left to the destination planet, the total distance between previous and destination and the pace the ship is moving at
    private Race race;      //the player's race - randomly assigned
    private int money;      //the money that the player has
    private boolean gameOver;       //if the player has lost
    private boolean fast, medium, slow;     //only one of these values can be true, they represent how fast the player is moving
    private boolean arrivedAtPlanet;        //states if player has just arrived at a planet
    private boolean firstMove;      //if it is the first move in the game
    public boolean justLoaded;      //used to tell if the game had just been loaded or not
    public ArrayList<String> crewNames;

    public Game() {
        crewNames = new ArrayList<String>();
        people = new ArrayList<Person>();
        planets = new ArrayList<Planet>();
        fast = false;
        medium = true;      //default speed is medium
        slow = false;
        firstMove = true;
        money = 100000;
        justLoaded = false;

        /* Create all 9 planets  */
        Planet Mercury = new Planet("Mercury");
        Planet Venus = new Planet("Venus");
        Planet Earth = new Planet("Earth");
        Planet Mars = new Planet("Mars");
        Planet Jupiter = new Planet("Jupiter");
        Planet Saturn = new Planet("Saturn");
        Planet Uranus = new Planet("Uranus");
        Planet Neptune = new Planet("Neptune");
        Planet Pluto = new Planet("Pluto");

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
        destination = new Planet("Temp");       //used as default destination until a planet is given
        previous = new Planet("Temp");
        race = new Race();
        gameOver = false;
        arrivedAtPlanet = false;
    }

    //Will automatically refresh the compound level to 100 if landing on a planet with the needed compound
    public boolean refreshCompound() {
        String compound = race.getStrength();
        if(destination.compound1.equals(compound) || destination.compound2.equals(compound)) {
            resources.setCompound(100);
            return true;
        }
        return false;
    }

    /* Fuel cost based on distanceRemaining from sun */
    public boolean sellFuel(int m) {
        double cost = destination.fuelCost;
        if(firstMove) {
            cost = 5;
        }
        money = money - (int)(cost * m);
        if(money < 0) {                         // make sure that this would not bankrupt
            System.out.println("You did not have enough money. The vendor has zero patience, and you have lost your chance to buy fuel.");
            money = money + (int)(cost * m);
            return false;
        }
        resources.incrementFuel(m, true);
        return true;
    }

    /* Food cost based on distanceRemaining from sun (medium range is cheapest) */
    public boolean sellFood(int m) {
        double cost = destination.foodCost;
        if(firstMove) {
            cost = 1;
        }
        money = money - (int)(cost * m);
        if(money < 0) {                         // make sure that this would not bankrupt
            System.out.println("You did not have enough money. The vendor has zero patience, and you have lost your chance to buy food.");
            money = money + (int)(cost * m);
            return false;
        }
        resources.incrementFood(m, true);
        return true;
    }

    /* Cheap on Earth, Mars, Pluto, and Mercury */
    public boolean sellAluminum(int m) {
        double cost = destination.aluminumCost;
        if(firstMove) {
            cost = 10;
        }
        money = money - (int)(cost * m);
        if(money < 0) {                         // make sure that this would not bankrupt
            System.out.println("You did not have enough money. The vendor has zero patience, and you have lost your chance to buy aluminum.");
            money = money + (int)(cost * m);
            return false;
        }
        resources.incrementAluminum(m, true);
        return true;
    }

    /* Cheap on Earth, Mars, Pluto, and Mercury */
    public boolean sellParts(int m, String s) {
        double cost = destination.partCost;
        if(firstMove) {
            cost = 15;
        }
        money = money - (int)(cost * m);
        if(money < 0) {                         // make sure that this would not bankrupt
            System.out.println("You did not have enough money. The vendor has zero patience, and you have lost your chance to buy spare parts.");
            money = money + (int)(cost * m);
            return false;
        }
        for (int i = 0; i < m; i++) {           // add 'm' of this spare part
            if(s.equals("engine")) {
                Part p = new Part("engine", 100);
                resources.addSpare(p);
            }else if(s.equals("wing")) {
                Part p = new Part("wing", 100);
                resources.addSpare(p);
            }else if(s.equals("livingBay")) {
                Part p = new Part("livingBay", 100);
                resources.addSpare(p);
            }
        }
        return true;
    }

    /* Makes moves towards planet destination */
    public String makeMove() {
        /* Things that need to be done in this method:
            - reduce distanceRemaining remaining
            - decide how much health each crew member loses
            - decide how to measure the pace (how many taps on the phone does it take?)
         */
        distanceRemaining -= pace;                       // reduce distanceRemaining remaining
        crewAttrition();                        // decide how much health each crew member loses
        resourceAttrition();                    // decide how many resources the crew loses
//        System.out.println("Destination: " + destination.name + " Distance Remaining: " + distanceRemaining + " Total Distance: " + totalDistance +" Pace: " + pace);
        if(distanceRemaining <= 0) {
            arrivedAtPlanet = true;
            destination.visited = true;
            refreshCompound();
        }
        String issue = getIssue();
        return issue;
    }

    //Handles the decrement of resources for each move; changes based off of player's speed
    public void resourceAttrition() {
        if(fast) {
            resources.incrementFuel(6, false);
            resources.incrementFood(5, false);
            resources.incrementCompound(3, false);
            ship.damagePart("engine", 3);
            ship.damagePart("wing", 3);
            ship.damagePart("livingBay", 3);
        }else if(medium) {
            resources.incrementFuel(3, false);
            resources.incrementFood(5, false);
            resources.incrementCompound(3, false);
            ship.damagePart("engine", 2);
            ship.damagePart("wing", 2);
            ship.damagePart("livingBay", 2);
        }else if(slow) {
            resources.incrementFuel(2, false);
            resources.incrementFood(5, false);
            resources.incrementCompound(3, false);
            ship.damagePart("engine", 1);
            ship.damagePart("wing", 1);
            ship.damagePart("livingBay", 1);
        }
    }

    //Handles the decrement of crew health for each move; changes based off of the player's race's strengths and weaknesses and the planet being traveled to
    public void crewAttrition() {
        /* Attrition will be measured based on destination planet
            - will be based on the compounds of the destination planet and race of the crew
            - will be different based on crew member (age, current condition)
        */
        for(int i = 0; i < people.size(); i++) {
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

    /*Will add a crew member to the ship*/
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

    //Returns ArrayList of people
    public ArrayList<Person> getPeople() {
        return people;
    }

    //Returns the person at a specific index
    public Person getCrew(int personIndex) {
        return people.get(personIndex);
    }

    //Sets the first destination to the correct planet from a given String - calls the index method
    public void setFirstDestination(String planet) {
        if(planet.equals("Mercury")) {
            setFirstDestination(0);
        }
        else if(planet.equals("Venus")) {
            setFirstDestination(1);
        }
        else if(planet.equals("Earth")) {
            setFirstDestination(2);
        }
        else if(planet.equals("Mars")) {
            setFirstDestination(3);
        }
        else if(planet.equals("Jupiter")) {
            setFirstDestination(4);
        }
        else if(planet.equals("Saturn")) {
            setFirstDestination(5);
        }
        else if(planet.equals("Uranus")) {
            setFirstDestination(6);
        }
        else if(planet.equals("Neptune")) {
            setFirstDestination(7);
        }
        else if(planet.equals("Pluto")) {
            setFirstDestination(8);
        }
    }

    //Sets the destination planet the player is traveling to - only used on first turn
    public void setFirstDestination(int planetIndex) {
        destination = planets.get(planetIndex);
        distanceRemaining = destination.distanceFromSun;
        totalDistance = destination.distanceFromSun;
        setSpeed(getSpeed());
    }

    //Sets the destination to the correct planet from a given String - calls the index method
    public void setDestination(String planet) {
        firstMove = false;
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
        else if(planet.equals("Pluto")) {
            setDestination(8);
        }
    }

    /*Will set the destination to the correct planet (MUST HAVE A DESTINATION PREVIOUSLY)*/
    public void setDestination(int planetIndex) {
        previous = destination;
        int currentIndex = 0;
        for(int i = 0; i < 9; i++) {
            if(planets.get(i).name.equals(destination.name)) {
                currentIndex = i;
                break;
            }
        }
        arrivedAtPlanet = false;

        /* Calculating the distanceRemaining between planets
            - each planet has 40 degrees between it and the next one
            - use planet index to compute total degrees between one planet and another
            - find sine value, and apply to destination planet's distanceRemaining
            */
        double hypotenuse = Math.sin(Math.toRadians(40 * (planetIndex - currentIndex)));
        destination = planets.get(planetIndex);
        distanceRemaining = (int)((destination.distanceFromSun) * hypotenuse);
        totalDistance = distanceRemaining;
        setSpeed(getSpeed());
    }

    //Sets the previous planet to the correct planet from a given String - calls the index method
    public void setPrevious(String planet) {
        if(planet.equals("Mercury")) {
            setPrevious(0);
        }
        else if(planet.equals("Venus")) {
            setPrevious(1);
        }
        else if(planet.equals("Earth")) {
            setPrevious(2);
        }
        else if(planet.equals("Mars")) {
            setPrevious(3);
        }
        else if(planet.equals("Jupiter")) {
            setPrevious(4);
        }
        else if(planet.equals("Saturn")) {
            setPrevious(5);
        }
        else if(planet.equals("Uranus")) {
            setPrevious(6);
        }
        else if(planet.equals("Neptune")) {
            setPrevious(7);
        }
        else if(planet.equals("Pluto")) {
            setPrevious(8);
        }
    }

    //Sets the previous planet to the correct planet - used index from planets ArrayList
    public void setPrevious(int planetIndex) {
        previous = planets.get(planetIndex);
    }

    /*Returns destination planet */
    public Planet getDestination() {
        return destination;
    }

    //Returns the previous planet
    public Planet getPrevious() {
        return previous;
    }

    /* Will set the races of all the crew members*/
    public void setCrewRace(Race r) {
        for(int i = 0; i < 5; i++) {
            people.get(i).setRace(r);
        }
        race = r;
    }

    public Race getRace() {
        return race;
    }

    //Sets distance remaining instance field
    public void setDistanceRemaining(double d) {
        distanceRemaining = d;
    }

    //Returns the distance remaining instance field
    public double getDistanceRemaining() {
        return distanceRemaining;
    }

    //Sets the total distance instance field
    public void setTotalDistance(double d) {
        totalDistance = d;
    }

    //Returns the total distance instance field
    public double getTotalDistance() {
        return totalDistance;
    }

    //Returns true if the player has went to all the planets
    public boolean isWinner() {
        int counter = 0;
        for(int i = 0; i < 9; i++) {
            if (planets.get(i).visited) {
                counter += 1;
            }
        }
        if(counter == 9) {
            System.out.println("You have visited all the planets.");
            return true;
        }
        return false;
    }

    //Returns true if the gameOver instance field is true - player has lost
    public boolean isLoser() {
        return gameOver;
    }

    //Sets the money instance field
    public void setMoney(int m) {
        money = m;
    }

    //Returns the money instance field
    public int getMoney() {
        return money;
    }

    //Uses planetary and current game data to determine if an issue will occur on a given turn
    //Returns a string saying the issue and will also set the gameOver field if the crew is killed
    //Semi-random - incorporates random values and planet data to determine if issue will happen
    private String getIssue() {
        String issue = "Successful Movement!";

        //resource and ship issues - these will all be game ending and have no random elements
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
        //health issues - issue chance of occurring is not random, but message displayed is random - is game ending if captain dies
        for (int i = 0; i < people.size(); ++i) {
            if (people.get(i).getCondition() <= 0) {
                if (i == 0) {
                    issue = "Your captain has died! Unfortunately, he did not name a second-in-command and the entire crew is killed in the resulting power struggle.";
                    if(people.size() == 1) {
                        issue = "Your captain has died! He was the last living crew member for you spaceship! Without anyone at the controls, the ship eventually crashes on a moon of Saturn and is destroyed.";
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
        if(totalDistance == distanceRemaining + pace && !previous.name.equals("Temp")) {
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
            double capturedChance = (previous.name.equals("Earth")) ? Math.random() : 1;
            if(capturedChance < .0001 && people.size() > 1) {               //.01% chance of this happening and the person needs to be leaving earth
                issue = "Your crew member " + people.get(people.size()-1).getName() + " has been captured by the indigenous species of planet Earth and is studied in Area 51. You do not see him again";
                people.remove(people.size()-1);
                return issue;
            }
        }
        //destination dependent issues
           //will encounter destination issues if within close distanceRemaining to destination planet
        if((distanceRemaining < pace*2 && slow) || (distanceRemaining < pace && (medium || fast))) {
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
                double instrumentMagneticIssueChance = (destination.globalMagneticField) ? Math.random() : 1;
                if(instrumentMagneticIssueChance < .025) {
                    issue = "Because of the global magnetic field of " + destination.name + " your navigation instruments are not working properly on the planet. Exploring the planet takes more time than usual.";
                    resources.incrementFood(10, false);
                    return issue;
                }
            }
            else {
                double moonIssueChance = Math.random();
                double ringIssueChance = (destination.ringSystem) ? Math.random() : 1;
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
        }
        //if traveling more than 500 units, the crew will enter cryosleep when traveling
        if(totalDistance > 500) {
            double cryosleepIssueChance = Math.random();
            if(cryosleepIssueChance < .0001) {
                int crewMember = (int)Math.floor(Math.random() * people.size());
                issue = people.get(crewMember).getName() + " has been killed due to complications with cryosleep. It was a painless way to go, but I am sure the rest of the crew will be sad to hear it when they wake up.";
                people.remove(crewMember);
                return issue;
            }
        }
        //if traveling at a fast pace
        if(fast) {
            double fastRisk = Math.random();
            if(fastRisk < .03) {
                issue = "Because of the fast speed your ship was traveling at, it was unable to avoid hitting a small meteor that the scanners did not pick up. All of your ship parts were damaged in the collision.";
                ship.damagePart("hull", 5);
                ship.damagePart("engine", 5);
                ship.damagePart("wing", 5);
                ship.damagePart("livingBay", 5);
                return issue;
            }
        }

        //miscellaneous issues
            //random issues that can occur
        double randomIssueChance = Math.random();
        if(randomIssueChance < .001) {
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

    //sets the pace to either fast(1), medium(2) or slow(3)
    public void setSpeed(int p) {
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

    //returns an int representing the pace for fast(1), medium(@) or slow(3)
    public int getSpeed() {      //returns 1 for fast, 2 for medium and 3 for slow
        if(fast) {
            return 1;
        }
        if(medium) {
            return 2;
        }
        return 3;
    }

    //returns the amount that the spaceship will move in one turn
    public double getPace(){
        return pace;
    }

    //returns boolean for if player has arrived at a planet
    public boolean getArrivedAtPlanet() {
        return arrivedAtPlanet;
    }

    //sets the boolean value for arrived at planet
    public void setArrivedAtPlanet(boolean b) {
        arrivedAtPlanet = b;
    }

    //returns the planets array
    public ArrayList<Planet> getPlanets() {
        return planets;
    }

    //set the visited value for a planet
    public void setVisited(String planetName) {
        if(planetName.equals("Mercury")) {
            planets.get(0).visited = true;
        }
        else if(planetName.equals("Venus")) {
            planets.get(1).visited = true;
        }
        else if(planetName.equals("Earth")) {
            planets.get(2).visited = true;
        }
        else if(planetName.equals("Mars")) {
            planets.get(3).visited = true;
        }
        else if(planetName.equals("Jupiter")) {
            planets.get(4).visited = true;
        }
        else if(planetName.equals("Saturn")) {
            planets.get(5).visited = true;
        }
        else if(planetName.equals("Uranus")) {
            planets.get(6).visited = true;
        }
        else if(planetName.equals("Neptune")) {
            planets.get(7).visited = true;
        }
    }

    //repairs the hull using the given amount of aluminum
    //returns if the hull was repaired
    public boolean repairHull(int amountAluminum) {
        if(resources.getAluminum() < amountAluminum) {
            return false;
        }
        resources.incrementAluminum(amountAluminum, false);
        ship.repairPart("hull", amountAluminum);
        return true;
    }

    //repairs the engine by using a spare wing
    //returns if the engine was repaired
    public boolean repairEngine() {
        if(resources.removeSpare("engine")){
            ship.setEngineStatus(100);
            gameOver=false;
            return true;
        }
        return false;
    }

    //repairs the wing by using a spare wing
    //returns if the wing was repaired
    public boolean repairWing() {
        if(resources.removeSpare("wing")){
            ship.setWingStatus(100);
            gameOver=false;
            return true;
        }
        return false;
    }

    //repairs the living bay using a spare living bay
    //returns if living bay was repaired
    public boolean repairLivingBay() {
        if(resources.removeSpare("livingBay")){
            ship.setLivingBayStatus(100);
            gameOver=false;
            return true;
        }
        return false;
    }

    public void showResources() {
        System.out.println("[Food] : " + resources.getFood());
        System.out.println("[Fuel] : " + resources.getFuel());
        System.out.println("[Aluminum] : " + resources.getAluminum());
        System.out.println("[" + race.getStrength() + "] : " + resources.getCompound());
        int wings = 0;
        int engines = 0;
        int livingBays = 0;
        for(int i = 0; i < resources.getSpares().size(); i++) {
            if(resources.getSpares().get(i).getName().equals("engine")) { engines = engines + 1; }
            else if(resources.getSpares().get(i).getName().equals("wing")) { wings = wings + 1; }
            else if(resources.getSpares().get(i).getName().equals("livingBay")) { livingBays = livingBays + 1; }
        }
        System.out.println("[Spare wings] : " + wings);
        System.out.println("[Spare engines] : " + engines);
        System.out.println("[Spare living bays] : " + livingBays);
    }

    public void showShip() {
        System.out.println("[Hull] : " + ship.getHullStatus());
        System.out.println("[Engine] : " + ship.getEngineStatus());
        System.out.println("[Wing] : " + ship.getWingStatus());
        System.out.println("[Living Bay] : " + ship.getLivingBayStatus());

    }

    //returns if it is first move of game
    public boolean getFirstMove() {
        return firstMove;
    }

    public void setPlanetInputStream(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder out = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < planets.size(); ++i) {
            planets.get(i).setInputStream(new ByteArrayInputStream(out.toString().getBytes(StandardCharsets.UTF_8)));
        }
    }

    public int getDestinationIndex() {
        return planets.indexOf(destination);
    }

    public double distanceToPlanet(Planet p) {
        if(firstMove){
            return(p.distanceFromSun);
        }
        else {
            int currentIndex = 0;
            int pIndex = 0;
            for (int i = 0; i < 9; i++) {
                if (planets.get(i).name.equals(destination.name)) {
                    currentIndex = i;
                }
                if (planets.get(i).name.equals(p.name)) {
                    pIndex = i;
                }
            }

        /* Calculating the distanceRemaining between planets
            - each planet has 40 degrees between it and the next one
            - use planet index to compute total degrees between one planet and another
            - find sine value, and apply to destination planet's distanceRemaining
            */
            double hypotenuse = Math.sin(Math.toRadians(40 * (pIndex - currentIndex)));
            return ((int) ((destination.distanceFromSun) * hypotenuse));
        }
    }

    public void setFirstMove(boolean b) {
        firstMove = b;
    }
}