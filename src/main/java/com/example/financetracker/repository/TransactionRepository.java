package com.example.financetracker.repository;

import com.example.financetracker.entity.Transaction;
import com.example.financetracker.entity.TransactionType;
import com.example.financetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserOrderByDateDesc(User user);

    List<Transaction> findByUserAndDateBetweenOrderByDateDesc(
            User user, LocalDate startDate, LocalDate endDate);

    @Query("SELECT t.category, SUM(t.amount) FROM Transaction t " +
            "WHERE t.user = :user AND t.type = :type AND " +
            "MONTH(t.date) = :month AND YEAR(t.date) = :year " +
            "GROUP BY t.category")
    List<Object[]> getCategoryTotals(@Param("user") User user,
                                     @Param("type") TransactionType type,
                                     @Param("month") int month,
                                     @Param("year") int year);
}
