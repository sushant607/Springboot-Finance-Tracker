package com.example.financetracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
@Table(name = "budgets")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Category is required")
    @Column(nullable = false)
    private String category;

    @NotNull(message = "Budget limit is required")
    @Positive(message = "Budget limit must be positive")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal budgetLimit;

//    @NotNull(message = "Month is required")
    @Column(name = "`month`", nullable = false) // Reserved word fix
    private Integer month;

//    @NotNull(message = "Year is required")
    @Column(name = "`year`", nullable = false) // Reserved word fix
    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Budget() {}

    public Budget(String category, BigDecimal budgetLimit, Integer month, Integer year, User user) {
        this.category = category;
        this.budgetLimit = budgetLimit;
        this.month = month;
        this.year = year;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getBudgetLimit() { return budgetLimit; }
    public void setBudgetLimit(BigDecimal budgetLimit) { this.budgetLimit = budgetLimit; }
    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
