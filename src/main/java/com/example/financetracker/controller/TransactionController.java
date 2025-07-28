package com.example.financetracker.controller;

import com.example.financetracker.entity.Transaction;
import com.example.financetracker.entity.TransactionType;
import com.example.financetracker.entity.User;
import com.example.financetracker.service.TransactionService;
import com.example.financetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String transactions(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        List<Transaction> transactions = transactionService.getUserTransactions(user);
        List<String> categories = transactionService.getCommonCategories();

        model.addAttribute("transactions", transactions);
        model.addAttribute("newTransaction", new Transaction());
        model.addAttribute("categories", categories);
        model.addAttribute("transactionTypes", TransactionType.values());

        return "transactions";
    }

    @PostMapping
    public String addTransaction(@Valid @ModelAttribute("newTransaction") Transaction transaction,
                                 BindingResult bindingResult,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        if (bindingResult.hasErrors()) {
            User user = userService.findByUsername(authentication.getName());
            List<Transaction> transactions = transactionService.getUserTransactions(user);
            List<String> categories = transactionService.getCommonCategories();

            model.addAttribute("transactions", transactions);
            model.addAttribute("categories", categories);
            model.addAttribute("transactionTypes", TransactionType.values());
            return "transactions";
        }

        User user = userService.findByUsername(authentication.getName());
        transaction.setUser(user);

        if (transaction.getDate() == null) {
            transaction.setDate(LocalDate.now());
        }

        transactionService.addTransaction(transaction);
        redirectAttributes.addFlashAttribute("success", "Transaction added successfully!");

        return "redirect:/transactions";
    }

    @GetMapping("/edit/{id}")
    public String editTransaction(@PathVariable Long id, Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Transaction transaction = transactionService.findById(id);

        // Security check - ensure user owns this transaction
        if (!transaction.getUser().getId().equals(user.getId())) {
            return "redirect:/transactions?error=unauthorized";
        }

        List<String> categories = transactionService.getCommonCategories();

        model.addAttribute("transaction", transaction);
        model.addAttribute("categories", categories);
        model.addAttribute("transactionTypes", TransactionType.values());

        return "edit-transaction";
    }

    @PostMapping("/update")
    public String updateTransaction(@Valid @ModelAttribute Transaction transaction,
                                    BindingResult bindingResult,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {

        if (bindingResult.hasErrors()) {
            List<String> categories = transactionService.getCommonCategories();
            model.addAttribute("categories", categories);
            model.addAttribute("transactionTypes", TransactionType.values());
            return "edit-transaction";
        }

        User user = userService.findByUsername(authentication.getName());
        transaction.setUser(user);

        transactionService.updateTransaction(transaction);
        redirectAttributes.addFlashAttribute("success", "Transaction updated successfully!");

        return "redirect:/transactions";
    }

    @PostMapping("/delete/{id}")
    public String deleteTransaction(@PathVariable Long id,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes) {

        User user = userService.findByUsername(authentication.getName());
        Transaction transaction = transactionService.findById(id);

        // Security check - ensure user owns this transaction
        if (!transaction.getUser().getId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized action!");
            return "redirect:/transactions";
        }

        transactionService.deleteTransaction(id);
        redirectAttributes.addFlashAttribute("success", "Transaction deleted successfully!");

        return "redirect:/transactions";
    }
}
