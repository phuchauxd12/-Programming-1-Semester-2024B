package utils;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Opinion {
    public static int getOption(Scanner input){
        boolean validInput = false;
        int option =0;
        while (!validInput) {
            try {
                System.out.println("Enter an option:");
                option = input.nextInt();
                validInput = true; // If input is valid, exit the loop
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number");
                input.next(); // Clear the invalid input from the scanner buffer
            }
        }
        return option;
    }
}
