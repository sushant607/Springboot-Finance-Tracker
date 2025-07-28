package com.example.financetracker.service;

import com.example.financetracker.entity.Transaction;
import com.example.financetracker.entity.TransactionType;
import com.example.financetracker.entity.User;
import com.example.financetracker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction addTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getUserTransactions(User user) {
        return transactionRepository.findByUserOrderByDateDesc(user);
    }

    public List<Transaction> getTransactionsByDateRange(User user, LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByUserAndDateBetweenOrderByDateDesc(user, startDate, endDate);
    }

    public Transaction findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    public Transaction updateTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Map<String, BigDecimal> getCategoryTotals(User user, TransactionType type, int month, int year) {
        List<Object[]> results = transactionRepository.getCategoryTotals(user, type, month, year);
        Map<String, BigDecimal> categoryTotals = new HashMap<>();

        for (Object[] result : results) {
            String category = (String) result[0];
            BigDecimal total = (BigDecimal) result[1];
            categoryTotals.put(category, total);
        }

        return categoryTotals;
    }

    public BigDecimal getTotalIncome(User user, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = getTransactionsByDateRange(user, startDate, endDate);
        return transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalExpenses(User user, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = getTransactionsByDateRange(user, startDate, endDate);
        return transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<String> getCommonCategories() {
        return List.of(
                "Food & Dining", "Transportation", "Shopping", "Entertainment",
                "Bills & Utilities", "Healthcare", "Education", "Travel",
                "Salary", "Freelance", "Investment", "Other"
        );
    }
}
