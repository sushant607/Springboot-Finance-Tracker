package com.example.financetracker.repository;

import com.example.financetracker.entity.Budget;
import com.example.financetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUserAndMonthAndYear(User user, Integer month, Integer year);
    Optional<Budget> findByUserAndCategoryAndMonthAndYear(
            User user, String category, Integer month, Integer year);
    List<Budget> findByUserOrderByYearDescMonthDesc(User user);
}
