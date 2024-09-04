package user;

import transaction.SaleTransactionList;

import java.time.LocalDate;

public class Employee extends User {


    public Employee(String userName, String password, String name, LocalDate dob, String address, int phoneNum, String email, ROLE userType) throws Exception {
        super(userName, password, name, dob, address, phoneNum, email, userType);
    }

    public Employee(String userName, String password, String name, LocalDate dob, String address, int phoneNum, String email, ROLE userType, String status, SaleTransactionList saleTransactionList, Object o) {
    }


//    public void generateOwnStatistics(LocalDate startDate, LocalDate endDate) {
//        double totalSalesRevenue = 0.0;
//        double totalServiceRevenue = 0.0;
//
//        int transactionCount = 0;
//        int serviceCount = 0;
//
//        // Revenue by client
//        Map<String, Double> clientRevenue = new HashMap<>();
//
//        // Total sales revenue
//        for (SaleTransaction transaction : saleTransactionList.getAllSaleTransactions()) {
//            if ((transaction.getTransactionDate().isEqual(startDate) || transaction.getTransactionDate().isAfter(startDate)) &&
//                    (transaction.getTransactionDate().isEqual(endDate) || transaction.getTransactionDate().isBefore(endDate))) {
//                totalSalesRevenue += transaction.getTotalAmount();
//                transactionCount++;
//
//                // Client revenue
//                clientRevenue.put(transaction.getClientId(), clientRevenue.getOrDefault(transaction.getClientId(), 0.0) + transaction.getTotalAmount());
//            }
//        }
//
//        // Total service revenue
//        for (Service service : serviceList.getAllServices()) {
//            if ((service.getServiceDate().isEqual(startDate) || service.getServiceDate().isAfter(startDate)) &&
//                    (service.getServiceDate().isEqual(endDate) || service.getServiceDate().isBefore(endDate))) {
//                totalServiceRevenue += service.getServiceCost();
//                serviceCount++;
//            }
//        }
//
//        System.out.printf("Statistics from %s to %s:\n", startDate, endDate);
//        System.out.printf("Total Sales Revenue: $%.2f\n", totalSalesRevenue);
//        System.out.printf("Total Number of Transactions: %d\n", transactionCount);
//        System.out.println();
//        System.out.printf("Total Service Revenue: $%.2f\n", totalServiceRevenue);
//        System.out.printf("Total Number of Services: %d\n", serviceCount);
//
//        System.out.println("--------------------------------------------------");
//        System.out.println("Revenue by Client:");
//        for (Map.Entry<String, Double> entry : clientRevenue.entrySet()) {
//            System.out.printf("Client ID: %s, Revenue: $%.2f\n", entry.getKey(), entry.getValue());
//        }
//    }
//
//    public void calculateRevenue(LocalDate startDate, LocalDate endDate) {
//        List<SaleTransaction> transactions = saleTransactionList.getSaleTransactionsBetween(startDate, endDate);
//        List<Service> services = serviceList.getServicesBetween(startDate, endDate);
//        double totalSaleRevenue = 0.0;
//        double totalServiceRevenue = 0.0;
//        for (SaleTransaction transaction : transactions) {
//            totalSaleRevenue += transaction.getTotalAmount();
//        }
//        for (Service service: services) {
//            totalSaleRevenue += service.getServiceCost();
//        }
//
//        System.out.printf("Total Revenue from %s to %s:\n", startDate, endDate);
//        System.out.printf("Sales Revenue: $%.2f\n", totalSaleRevenue);
//        System.out.printf("Service Revenue: $%.2f\n", totalServiceRevenue);
//        System.out.printf("Total Revenue: $%.2f\n", totalServiceRevenue);
//    }
//
//    public void listItemsSold(LocalDate startDate, LocalDate endDate) {
//        List<SaleTransaction> transactions = saleTransactionList.getSaleTransactionsBetween(startDate, endDate);
//        System.out.println("Listing items sold from " + startDate + " to " + endDate + ":");
//        for (SaleTransaction transaction : transactions) {
//            System.out.println(transaction.getFormattedSaleTransactionDetails());
//        }
//    }

    @Override
    public String toString() {
        return "Employee{" +
                "userID='" + userID + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", address='" + address + '\'' +
                ", phoneNum=" + phoneNum +
                ", email='" + email + '\'' +
                ", userType=" + userType +
                ", status='" + status + '\'' +
                '}';
    }
}
