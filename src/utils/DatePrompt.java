package utils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Scanner;

public class DatePrompt {
    public LocalDate promptForDate() {
        Scanner input = new Scanner(System.in);
        int day, month, year;

        while (true) {
            System.out.print("Please input the day (1-31): ");
            day = getValidInt(input, 1, 31);

            System.out.print("Please input the month (1-12): ");
            month = getValidInt(input, 1, 12);

            System.out.print("Please input the year (up to 2024): ");
            year = getValidInt(input, 1, 2024);

            if (isValidDate(year, month, day)) {
                break;
            } else {
                System.out.println("Invalid date. Please try again.");
            }
        }

        return LocalDate.of(year, month, day);
    }

    // Method to get a valid integer input from the user within a specified range
    private int getValidInt(Scanner input, int min, int max) {
        int value;
        while (true) {
            while (!input.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                input.next();
            }
            value = input.nextInt();
            if (value >= min && value <= max) {
                break;
            } else {
                System.out.printf("Please enter a number between %d and %d.\n", min, max);
            }
        }
        return value;
    }

    private boolean isValidDate(int year, int month, int day) {
        try {
            YearMonth yearMonth = YearMonth.of(year, month);
            return day <= yearMonth.lengthOfMonth();
        } catch (Exception e) {
            return false;
        }
    }
}