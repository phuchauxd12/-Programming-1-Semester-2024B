import java.time.LocalDate;

import models.Service;
import models.ServiceBy;
import models.ServiceList;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        System.out.printf("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {
            System.out.println("i = " + i);
        }

        // ServiceList serviceList = new ServiceList();
        // Service service1 = new Service(LocalDate.of(2024, 8, 2), "c-123", "m-456", "Oil Change", ServiceBy.AUTO136, "car-101", 12.55);
        // service1.setAdditionalNotes("This is for testing!");
        // System.out.println(service1.getFormattedServiceDetails());
        // serviceList.addService(service1);
        // serviceList.getAllServices();
    }
}