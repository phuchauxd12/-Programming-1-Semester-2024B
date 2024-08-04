package transaction;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class SaleTransactionList {
    private List<SaleTransaction> transactions;

    public SaleTransactionList() {
        transactions = new ArrayList<>();
    }

    public void addSaleTransaction(SaleTransaction transaction) {
        if (transaction != null) {
            transactions.add(transaction);
        } else {
            throw new IllegalArgumentException("Cannot add null transaction.");
        }
    }

    public SaleTransaction getSaleTransactionById(String transactionId) {
        for (SaleTransaction transaction : transactions) {
            if (transaction.getTransactionId().equals(transactionId)) {
                return transaction;
            }
        }
        return null;
    }

    public List<SaleTransaction> getAllSaleTransactions() {
        return new ArrayList<>(transactions); // Return a copy to avoid modification
    }

    public void updateSaleTransaction(SaleTransaction updatedTransaction) {
        if (updatedTransaction != null) {
            for (int i = 0; i < transactions.size(); i++) {
                if (transactions.get(i).getTransactionId().equals(updatedTransaction.getTransactionId())) {
                    transactions.set(i, updatedTransaction);
                    return;
                }
            }
            throw new IllegalArgumentException("Transaction not found with ID: " + updatedTransaction.getTransactionId());
        } else {
            throw new IllegalArgumentException("Cannot update with null transaction.");
        }
    }

    public void deleteSaleTransaction(String transactionId) {
        boolean removed = transactions.removeIf(transaction -> transaction.getTransactionId().equals(transactionId));
        if (!removed) {
            throw new IllegalArgumentException("Transaction not found with ID: " + transactionId);
        }
    }

    public double calculateTotalSales() {
        double total = 0.0;
        for (SaleTransaction transaction : transactions) {
            total += transaction.getTotalAmount();
        }
        return total;
    }

    public List<SaleTransaction> getSaleTransactionsBetween(LocalDate startDate, LocalDate endDate) {
        List<SaleTransaction> filteredTransactions = new ArrayList<>();
        for (SaleTransaction transaction : transactions) {
            LocalDate transactionDate = transaction.getTransactionDate();
            if ((transactionDate.isEqual(startDate) || transactionDate.isAfter(startDate)) &&
                    (transactionDate.isEqual(endDate) || transactionDate.isBefore(endDate))) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }
}