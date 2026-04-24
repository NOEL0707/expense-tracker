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

    Optional<Expense> findByUserIdAndIdempotencyKey(UUID userId, String idempotencyKey);

    Optional<Expense> findByIdAndUserId(UUID id, UUID userId);

    @Query("SELECT e FROM Expense e WHERE e.user.id = :userId AND (:category IS NULL OR e.category = :category)")
    List<Expense> findExpensesWithFilters(@Param("userId") UUID userId, @Param("category") String category, Sort sort);

    void deleteByUserId(UUID userId);
}
