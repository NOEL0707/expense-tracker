package com.expensetracker.expensetracker.repository;

import com.expensetracker.expensetracker.model.Expense;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

    Optional<Expense> findByIdempotencyKey(String idempotencyKey);

    @Query("SELECT e FROM Expense e WHERE (:category IS NULL OR e.category = :category)")
    List<Expense> findExpensesWithFilters(@Param("category") String category, Sort sort);
}
