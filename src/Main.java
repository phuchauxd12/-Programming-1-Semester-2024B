import models.SaleTransaction;
import models.SaleTransactionList;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.printf("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {
            System.out.println("i = " + i);
        }

        SaleTransactionList transactionList = new SaleTransactionList();
        SaleTransaction transaction1 = new SaleTransaction(LocalDate.of(2024, 8, 2), "c-123", "s-456", 12.55, 32.00);
        SaleTransaction transaction2 = new SaleTransaction(LocalDate.of(2024, 8, 3), "c-222", "s-333", 12.55, 32.00);
        transactionList.addSaleTransaction(transaction1);
        transactionList.addSaleTransaction(transaction2);
        List<SaleTransaction>  allTransactions = transactionList.getAllSaleTransactions();
        for (SaleTransaction each : allTransactions) {
            System.out.println(each.getFormattedSaleTransactionDetails()); 
        }
    }
}