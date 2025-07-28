package com.example.financetracker.controller;

import com.example.financetracker.dto.BudgetStatus;
import com.example.financetracker.entity.Transaction;
import com.example.financetracker.entity.TransactionType;
import com.example.financetracker.entity.User;
import com.example.financetracker.service.BudgetService;
import com.example.financetracker.service.TransactionService;
import com.example.financetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final UserService userService;
    private final TransactionService transactionService;
    private final BudgetService budgetService;

    @Autowired
    public HomeController(UserService userService,
                          TransactionService transactionService,
                          BudgetService budgetService) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.budgetService = budgetService;
    }

    @GetMapping("/")
    public String dashboard(Model model, Authentication authentication) {
        try {
            // Get user safely
            User user = userService.findByUsername(authentication.getName());
            LocalDate now = LocalDate.now();

            // Get current month data with null safety
            LocalDate startOfMonth = now.withDayOfMonth(1);
            LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());

            List<Transaction> monthlyTransactions = transactionService != null ?
                    transactionService.getTransactionsByDateRange(user, startOfMonth, endOfMonth) :
                    new ArrayList<>();

            List<Transaction> recentTransactions = monthlyTransactions.stream()
                    .limit(10)
                    .collect(Collectors.toList());

            // Calculate financial summary with null safety
            BigDecimal totalIncome = transactionService != null ?
                    transactionService.getTotalIncome(user, startOfMonth, endOfMonth) :
                    BigDecimal.ZERO;

            BigDecimal totalExpenses = transactionService != null ?
                    transactionService.getTotalExpenses(user, startOfMonth, endOfMonth) :
                    BigDecimal.ZERO;

            BigDecimal balance = totalIncome.subtract(totalExpenses);

            // Get budget status with null safety
            Map<String, BudgetStatus> budgetStatus = budgetService != null ?
                    budgetService.getBudgetStatus(user, now.getMonthValue(), now.getYear()) :
                    new HashMap<>();

            // Calculate budget summary with null safety
            BigDecimal totalBudget = budgetService != null ?
                    budgetService.getTotalBudgetAmount(user, now.getMonthValue(), now.getYear()) :
                    BigDecimal.ZERO;

            BigDecimal totalSpent = budgetService != null ?
                    budgetService.getTotalSpentAmount(user, now.getMonthValue(), now.getYear()) :
                    BigDecimal.ZERO;

            // Get category breakdown for expenses with null safety
            Map<String, BigDecimal> expensesByCategory = transactionService != null ?
                    transactionService.getCategoryTotals(user, TransactionType.EXPENSE, now.getMonthValue(), now.getYear()) :
                    new HashMap<>();

            // Count exceeded budgets
            long exceededBudgets = budgetStatus.values().stream()
                    .mapToLong(status -> status.getPercentage() > 100 ? 1 : 0)
                    .sum();

            // Add all data to model
            model.addAttribute("user", user);
            model.addAttribute("recentTransactions", recentTransactions);
            model.addAttribute("totalIncome", totalIncome);
            model.addAttribute("totalExpenses", totalExpenses);
            model.addAttribute("balance", balance);
            model.addAttribute("budgetStatus", budgetStatus);
            model.addAttribute("totalBudget", totalBudget);
            model.addAttribute("totalSpent", totalSpent);
            model.addAttribute("expensesByCategory", expensesByCategory);
            model.addAttribute("exceededBudgets", exceededBudgets);
            model.addAttribute("currentMonth", now.getMonth().name());
            model.addAttribute("currentYear", now.getYear());
            model.addAttribute("transactionCount", monthlyTransactions.size());
            model.addAttribute("budgetCount", budgetStatus.size());

            return "dashboard";

        } catch (Exception e) {
            // Log the error and return safe dashboard
            System.err.println("Dashboard error: " + e.getMessage());
            e.printStackTrace();

            // Provide minimal safe data
            model.addAttribute("user", userService.findByUsername(authentication.getName()));
            model.addAttribute("recentTransactions", new ArrayList<>());
            model.addAttribute("totalIncome", BigDecimal.ZERO);
            model.addAttribute("totalExpenses", BigDecimal.ZERO);
            model.addAttribute("balance", BigDecimal.ZERO);
            model.addAttribute("budgetStatus", new HashMap<>());
            model.addAttribute("totalBudget", BigDecimal.ZERO);
            model.addAttribute("totalSpent", BigDecimal.ZERO);
            model.addAttribute("expensesByCategory", new HashMap<>());
            model.addAttribute("exceededBudgets", 0L);
            model.addAttribute("currentMonth", LocalDate.now().getMonth().name());
            model.addAttribute("currentYear", LocalDate.now().getYear());
            model.addAttribute("transactionCount", 0);
            model.addAttribute("budgetCount", 0);

            return "dashboard";
        }
    }
}
