package com.example.financetracker.service;

import com.example.financetracker.dto.BudgetStatus;
import com.example.financetracker.entity.Budget;
import com.example.financetracker.entity.TransactionType;
import com.example.financetracker.entity.User;
import com.example.financetracker.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final TransactionService transactionService;

    @Autowired
    public BudgetService(BudgetRepository budgetRepository,
                         TransactionService transactionService) {
        this.budgetRepository = budgetRepository;
        this.transactionService = transactionService;
    }

    public Budget saveBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    public Budget findById(Long id) {
        return budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));
    }

    public List<Budget> getUserBudgets(User user, int month, int year) {
        return budgetRepository.findByUserAndMonthAndYear(user, month, year);
    }

    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }

    public Budget updateBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    public Optional<Budget> findExistingBudget(User user, String category, int month, int year) {
        return budgetRepository.findByUserAndCategoryAndMonthAndYear(user, category, month, year);
    }

    public Map<String, BudgetStatus> getBudgetStatus(User user, int month, int year) {
        List<Budget> budgets = getUserBudgets(user, month, year);
        Map<String, BigDecimal> expenseTotals = transactionService
                .getCategoryTotals(user, TransactionType.EXPENSE, month, year);

        Map<String, BudgetStatus> statusMap = new HashMap<>();
        for (Budget b : budgets) {
            BigDecimal spent     = expenseTotals.getOrDefault(b.getCategory(), BigDecimal.ZERO);
            BigDecimal remaining = b.getBudgetLimit().subtract(spent);

            // Calculate percentage without rounding mode
            double percentage = 0.0;
            if (b.getBudgetLimit().compareTo(BigDecimal.ZERO) > 0) {
                try {
                    // Use divide without rounding mode - will throw exception if exact division is not possible
                    BigDecimal ratio = spent.divide(b.getBudgetLimit());
                    percentage = ratio.multiply(BigDecimal.valueOf(100)).doubleValue();
                } catch (ArithmeticException e) {
                    // If exact division is not possible, use double division as fallback
                    percentage = spent.doubleValue() / b.getBudgetLimit().doubleValue() * 100.0;
                }
            }

            statusMap.put(b.getCategory(),
                    new BudgetStatus(b.getBudgetLimit(), spent, remaining, percentage));
        }
        return statusMap;
    }

    public BigDecimal getTotalBudgetAmount(User user, int month, int year) {
        return getUserBudgets(user, month, year)
                .stream()
                .map(Budget::getBudgetLimit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalSpentAmount(User user, int month, int year) {
        return transactionService
                .getCategoryTotals(user, TransactionType.EXPENSE, month, year)
                .values()
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
