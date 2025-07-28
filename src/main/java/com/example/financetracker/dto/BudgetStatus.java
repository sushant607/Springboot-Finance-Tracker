package com.example.financetracker.dto;

import java.math.BigDecimal;

public class BudgetStatus {
    private final BigDecimal budgetLimit;
    private final BigDecimal spent;
    private final BigDecimal remaining;
    private final double percentage;

    public BudgetStatus(BigDecimal budgetLimit,
                        BigDecimal spent,
                        BigDecimal remaining,
                        double percentage) {
        this.budgetLimit = budgetLimit;
        this.spent = spent;
        this.remaining = remaining;
        this.percentage = percentage;
    }

    public BigDecimal getBudgetLimit() { return budgetLimit; }
    public BigDecimal getSpent()       { return spent; }
    public BigDecimal getRemaining()   { return remaining; }
    public double     getPercentage()  { return percentage; }
}
