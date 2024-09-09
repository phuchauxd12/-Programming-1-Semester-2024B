package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class DatePrompt {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static LocalDate getDate(String prompt) {
        Scanner scanner = new Scanner(System.in);
        LocalDate startDate;
        while (true) {
            System.out.printf("Enter %s date (dd/MM/yyyy): ", prompt);
            try {
                String input = sanitizeDateInput(scanner.nextLine());
                startDate = validateAndParseDate(input);
                break;
            } catch (DateTimeParseException | IllegalArgumentException | IndexOutOfBoundsException e) {
                System.out.println("Invalid start date. Please try again.");
            }
        }
        return startDate;
    }

    public static LocalDate getEndDate(LocalDate startDate) {
        Scanner scanner = new Scanner(System.in);
        LocalDate endDate;
        while (true) {
            System.out.print("Enter end date (dd/MM/yyyy): ");
            try {
                String input = sanitizeDateInput(scanner.nextLine());
                endDate = validateAndParseDate(input);
                if (!endDate.isBefore(startDate)) {
                    break;
                }
                System.out.println("End date cannot be before start date. Please try again.");
            } catch (DateTimeParseException | IllegalArgumentException | IndexOutOfBoundsException e) {
                System.out.println("Invalid end date. Please try again.");
            }
        }
        return endDate;
    }

    public static String sanitizeDateInput(String input) {
        // Split the input by "/"
        String[] dateParts = input.split("/");

        // Ensure each part (day, month, year) is correctly formatted
        String day = (dateParts[0].length() == 1) ? "0" + dateParts[0] : dateParts[0];
        String month = (dateParts[1].length() == 1) ? "0" + dateParts[1] : dateParts[1];
        String year = dateParts[2];

        // Join back the sanitized parts into a correctly formatted date string
        return day + "/" + month + "/" + year;
    }

    public static LocalDate validateAndParseDate(String dateStr) {
        LocalDate parsedDate = LocalDate.parse(dateStr, formatter);
        // Additional validation to ensure the date is legitimate (e.g., no 30th February)
        if (!parsedDate.format(formatter).equals(dateStr)) {
            throw new IllegalArgumentException("Invalid date.");
        }
        return parsedDate;
    }
}