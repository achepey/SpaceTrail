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
    }

}
