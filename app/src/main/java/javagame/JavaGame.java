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
public class JavaGame {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        Game test = new Game();

        /* Declare captain of ship */
        System.out.print("Welcome to Space Trail. What would you like your captain's name to be?\n: ");
        String captain = s.nextLine();
        test.addCrew(captain, true);

         /* Add the crew (4) */
        System.out.print("Please enter name for [Crew Member 1] : ");
        String crew1 = s.nextLine();
        test.addCrew(crew1, false);
        System.out.print("Please enter name for [Crew Member 2] : ");
        String crew2 = s.nextLine();
        test.addCrew(crew2, false);
        System.out.print("Please enter name for [Crew Member 3] : ");
        String crew3 = s.nextLine();
        test.addCrew(crew3, false);
        System.out.print("Please enter name for [Crew Member 4] : ");
        String crew4 = s.nextLine();
        test.addCrew(crew4, false);

        /* Declare race of crew */
        System.out.println("Cpt. " + captain + " and the crew are members of the following race: ");
        Race race = new Race();
        test.setCrewRace(race);

        /* Purchase materials */
        System.out.println("Time to buy materials for the journey.");
        System.out.print("[Food] costs $1 per unit. How many units would you like to buy?\n: ");
        int food = s.nextInt();
        test.sellFood(food);
        System.out.print("[Fuel] costs $5 per unit. How many units would you like to buy?\n: ");
        int fuel = s.nextInt();
        test.sellFood(fuel);
        System.out.print("[Aluminum] costs $10 per unit. How many units would you like to buy?\n: ");
        int aluminum = s.nextInt();
        test.sellFood(aluminum);
        System.out.print("[Part] costs $15 per unit. How many units would you like to buy?\n: ");
        int part = s.nextInt();
        test.sellFood(part);

        /* Set first destination */
        System.out.println("What planet would you like to travel to first?");
        System.out.print("1. [Mercury]\n2.[Venus]\n3.[Earth]\n4. [Mars]\n5.[Jupiter]\n6. [Saturn]\n7. [Uranus]\n8. [Neptune]\n9. [Pluto]\n: ");
        int destination = s.nextInt();
        while (destination > 9 || destination < 1) {
            System.out.print("Incorrect choice. Choose a number between 1 and 9.\n: ");
            destination = s.nextInt();
        }
        test.setFirstDestination(destination - 1);

        /* Begin moving, with pace defaulted to medium */
        test.setPace(2);
        System.out.print("Hit [Enter] to move towards [" + test.getDestination().name + "].\n: ");
        String moving = s.nextLine();
        while (!test.getArrivedAtPlanet()) {
            String result = test.makeMove();
            System.out.println(result);
            if (test.isLoser()) {
                System.out.println("GAME OVER. YOU LOSE.");
                break;
            } else {
                System.out.print("Would you like to repair your ship, or change pace? (y/n)\n: ");
                String choice = s.nextLine();
                if (choice.equals("y")) {
                    System.out.print("Do you want to repair (1) or chance pace (2)?\n: ");
                    choice = s.nextLine();
                    if (choice.equals("1")) {
                        System.out.print("Would you like to repair the hull (1), engine (2), living bay (3), or wing (3)");
                        int repair = s.nextInt();
                        //switch(repair) {
                        // case 1:
                        //     test.getShip().
                    }
                } else if (choice.equals("2")) {
                    System.out.println("What pace would you like to go at?");
                    System.out.print("1. [Fast]\n2.[Medium]\n3. [Slow]\n: ");
                    int pace = s.nextInt();
                    while (pace != 1 && pace != 2 && pace != 3) {
                        System.out.print("Incorrect choice. Choose a number between 1 and 3.\n: ");
                        pace = s.nextInt();
                    }
                    test.setPace(pace);
                }
            }
            System.out.print("Hit [Enter] to move towards [" + test.getDestination().name + "].\n: ");
            moving = s.nextLine();
        }
    }
}
