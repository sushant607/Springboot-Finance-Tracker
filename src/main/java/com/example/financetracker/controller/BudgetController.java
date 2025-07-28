package com.example.financetracker.controller;

import com.example.financetracker.dto.BudgetStatus;
import com.example.financetracker.entity.Budget;
import com.example.financetracker.entity.User;
import com.example.financetracker.service.BudgetService;
import com.example.financetracker.service.TransactionService;
import com.example.financetracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/budgets")
public class BudgetController {

    private final BudgetService budgetService;
    private final UserService userService;
    private final TransactionService transactionService;

    @Autowired
    public BudgetController(BudgetService budgetService,
                            UserService userService,
                            TransactionService transactionService) {
        this.budgetService = budgetService;
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @GetMapping
    public String budgets(Model model, Authentication auth) {
        try {
            User user = userService.findByUsername(auth.getName());
            LocalDate now = LocalDate.now();

            List<Budget> currentBudgets = budgetService.getUserBudgets(user, now.getMonthValue(), now.getYear());
            Map<String, BudgetStatus> budgetStatus = budgetService.getBudgetStatus(user, now.getMonthValue(), now.getYear());
            List<String> categories = transactionService.getCommonCategories();
            BigDecimal totalBudget = budgetService.getTotalBudgetAmount(user, now.getMonthValue(), now.getYear());
            BigDecimal totalSpent = budgetService.getTotalSpentAmount(user, now.getMonthValue(), now.getYear());

            // Debug logging
            System.out.println("Current budgets size: " + currentBudgets.size());
            System.out.println("Budget status size: " + budgetStatus.size());
            System.out.println("Categories size: " + categories.size());

            model.addAttribute("budgets", currentBudgets);
            model.addAttribute("budgetStatus", budgetStatus);
            model.addAttribute("categories", categories);
            model.addAttribute("newBudget", new Budget());
            model.addAttribute("currentMonth", now.getMonth().name());
            model.addAttribute("currentYear", now.getYear());
            model.addAttribute("totalBudget", totalBudget);
            model.addAttribute("totalSpent", totalSpent);

            return "budgets";
        } catch (Exception e) {
            System.err.println("Error in budgets controller: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/?error=budget";
        }
    }

    @PostMapping
    public String addBudget(@Valid @ModelAttribute("newBudget") Budget budget,
                            BindingResult br,
                            Authentication auth,
                            RedirectAttributes ra,
                            Model model) {
        try {
            System.out.println("Adding budget - Category: " + budget.getCategory() + ", Amount: " + budget.getBudgetLimit());

            if (br.hasErrors()) {
                System.out.println("Validation errors: " + br.getAllErrors());
                return budgets(model, auth);
            }

            User user = userService.findByUsername(auth.getName());
            LocalDate now = LocalDate.now();
            budget.setUser(user);
            budget.setMonth(now.getMonthValue());
            budget.setYear(now.getYear());

            Optional<Budget> exists = budgetService.findExistingBudget(
                    user, budget.getCategory(), budget.getMonth(), budget.getYear());

            if (exists.isPresent()) {
                ra.addFlashAttribute("error",
                        "Budget for  + budget.getCategory() + already exists this month.");
                return "redirect:/budgets";
            }

            budgetService.saveBudget(budget);
            ra.addFlashAttribute("success", "Budget added successfully!");
            System.out.println("Budget saved successfully!");
            return "redirect:/budgets";
        } catch (Exception e) {
            System.err.println("Error adding budget: " + e.getMessage());
            e.printStackTrace();
            ra.addFlashAttribute("error", "Error adding budget. Please try again.");
            return "redirect:/budgets";
        }
    }

    @GetMapping("/edit/{id}")
    public String editBudget(@PathVariable Long id,
                             Model model,
                             Authentication auth,
                             RedirectAttributes ra) {
        try {
            User user = userService.findByUsername(auth.getName());
            Budget b = budgetService.findById(id);

            if (!b.getUser().getId().equals(user.getId())) {
                ra.addFlashAttribute("error", "Unauthorized");
                return "redirect:/budgets";
            }

            model.addAttribute("budget", b);
            model.addAttribute("categories", transactionService.getCommonCategories());
            return "edit-budget";
        } catch (Exception e) {
            System.err.println("Error editing budget: " + e.getMessage());
            ra.addFlashAttribute("error", "Error loading budget for editing.");
            return "redirect:/budgets";
        }
    }

    @PostMapping("/update")
    public String updateBudget(@Valid @ModelAttribute Budget budget,
                               BindingResult br,
                               Authentication auth,
                               RedirectAttributes ra,
                               Model model) {
        try {
            if (br.hasErrors()) {
                model.addAttribute("categories", transactionService.getCommonCategories());
                return "edit-budget";
            }

            User user = userService.findByUsername(auth.getName());
            budget.setUser(user);
            budgetService.updateBudget(budget);
            ra.addFlashAttribute("success", "Budget updated successfully!");
            return "redirect:/budgets";
        } catch (Exception e) {
            System.err.println("Error updating budget: " + e.getMessage());
            ra.addFlashAttribute("error", "Error updating budget. Please try again.");
            return "redirect:/budgets";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteBudget(@PathVariable Long id,
                               Authentication auth,
                               RedirectAttributes ra) {
        try {
            User user = userService.findByUsername(auth.getName());
            Budget b = budgetService.findById(id);

            if (!b.getUser().getId().equals(user.getId())) {
                ra.addFlashAttribute("error", "Unauthorized");
                return "redirect:/budgets";
            }

            budgetService.deleteBudget(id);
            ra.addFlashAttribute("success", "Budget deleted successfully!");
            return "redirect:/budgets";
        } catch (Exception e) {
            System.err.println("Error deleting budget: " + e.getMessage());
            ra.addFlashAttribute("error", "Error deleting budget. Please try again.");
            return "redirect:/budgets";
        }
    }
}
